import json
import argparse

# Create argument parser and add arguments
parser = argparse.ArgumentParser(description='Parse filtered logs and assessment instructions')
parser.add_argument('logs', type=str, help='Path to filtered_logs.json')
parser.add_argument('assessment_instructions', type=str, help='Path to QKART_ASSESSMENT_INSTRUCTIONS.json')
args = parser.parse_args()

# Load filtered logs
with open(args.logs, 'r') as f:
    logs = json.load(f)

# Load assessment instructions
with open(args.assessment_instructions, 'r') as f:
    assessment_instructions = json.load(f)

def evaluator(logical_unit,start_point):
    # Initialize variable to keep track of last "PASS" index
    last_pass_index = start_point
    test_cases = []
    points = 0
    result = {
        "logical_unit": logical_unit['LU'],
        "description": logical_unit['LU_description'],
        "hint": logical_unit['hint'],
        "passed": True 
    }
    lookup = logical_unit['lookup']
    lu_start = True
    for instruction in logical_unit["instruction_set"]:
        # Initialize dictionary for test case
        test_case = {
            "test_description": instruction["description"],
            "Passed": False,
            "is_optional": instruction["optional"],
            "Points": 0
        }
        
        if lu_start:
            real_lookup = len(logs['actions'])
            lu_start = False
        else:
            real_lookup =last_pass_index+lookup
        # Iterate over logs starting from last "PASS" index

        for i in range(last_pass_index, real_lookup):
            log = logs['actions'][i]
            flag = True
            # Check if log matches validation criteria
            for val in instruction['validations']:
                if log.get(val["object_notation"]):
                    if not any([match.lower() in log[val['object_notation']].lower() for match in val['expected_value']]):
                        flag = False
                        break
            # Test case passed
            if flag:
                test_case["Passed"] = True
                test_case["Points"] = 1
                points += 1
                # Update last "PASS" index
                last_pass_index = i + 1
                break
        # Add test case to list
        test_cases.append(test_case)
    if not all([test['Passed'] for test in test_cases]):
        result['failure_analysis'] = test_cases
        result['passed'] = False
    return result, last_pass_index

def break_into_lu():
    # Iterate over assessment instructions
    results =[]
    result = None
    for logical_unit in assessment_instructions:
        if logical_unit['repeat']:
            start_point = 0
            while True:
                result, end_point = evaluator(logical_unit, start_point)
                results.append(result)
                if(start_point==end_point):
                    break
                start_point = end_point
        else:
            result, __ = evaluator(logical_unit)
        
    print(json.dumps(results[:-1], indent=4))
    
break_into_lu()