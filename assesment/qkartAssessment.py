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
        "passed": True ,
        "total_points": 0
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
                    if val['operator'] == 'not_contains':
                        if any([match.lower() in log[val['object_notation']].lower() for match in val['expected_value']]):
                            flag = False
                            break
                    else:
                        if not any([match.lower() in log[val['object_notation']].lower() for match in val['expected_value']]):
                            flag = False
                            break
            # Test case passed
            if flag:
                test_case["Passed"] = True
                test_case["Points"] = 1
                points += 1
                # Update last "PASS" index
                last_pass_index = i +1
                # print(json.dumps(log,indent=4))
                # print(json.dumps(test_case, indent=4))
                break
        # Add test case to list
        test_cases.append(test_case)
    if not all([test['Passed'] for test in test_cases]):
        result['failure_analysis'] = test_cases
        result['passed'] = False
    result['total_points'] = points
    return result, last_pass_index


def parse_results(original_dict):
    # Create a new dictionary to store the filtered data
    filtered_dict = {}  
    flag = True

    # Loop through each item in the original dictionary
    for item in original_dict:
        
        # If the logical unit is not already in the filtered dictionary, add it
        if item['logical_unit'] not in filtered_dict:
            filtered_dict[item['logical_unit']] = item
            flag = True
            
        # If the logical unit is already in the filtered dictionary, update it with the higher score or passed value
        elif flag:
            existing_item = filtered_dict[item['logical_unit']]
            if item['passed'] == True:
                existing_item['passed'] = True
                filtered_dict[item['logical_unit']] = item
                flag = False
            else:
                existing_points = existing_item['total_points']
                new_points = item['total_points']
                if new_points > existing_points:
                    existing_item['total_points'] = new_points
                    existing_item['failure_analysis'] = item['failure_analysis']
                filtered_dict[item['logical_unit']] = existing_item
    return filtered_dict

def break_into_lu():
    # Iterate over assessment instructions
    results =[]
    result = None
    for logical_unit in assessment_instructions:
        if logical_unit['is_enabled']:
            start_point = 0
            while True:
                result, end_point = evaluator(logical_unit, start_point)
                results.append(result)
                if(start_point==end_point):
                    break
                start_point = end_point
    final = parse_results(results)
    print(json.dumps(final, indent=4))
    
break_into_lu()