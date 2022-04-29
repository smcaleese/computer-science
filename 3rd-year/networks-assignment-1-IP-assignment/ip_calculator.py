from helper_functions import get_ip_class, get_first_and_last_address, to_binary_string_list, to_decimal_dot, bin_string_to_bin_list
from ip_class_info import ip_class_info_d

def get_class_stats(ip_addr):
    print("ip_addr:", ip_addr)
    # get class and then use dictionary to get rest of information
    ip_addr_binary_list = to_binary_string_list(ip_addr)
    first_octet = ip_addr_binary_list[0]

    ip_class = get_ip_class(first_octet)
    if ip_class == "D" or ip_class == "E":
        num_networks = "N/A"
        num_hosts = "N/A"
    else:
        num_networks = 2 ** ip_class_info_d[ip_class]["network_bits"]
        num_hosts = 2 ** ip_class_info_d[ip_class]["host_bits"]
    first_address, last_address = get_first_and_last_address(ip_class)

    output = ("Class: {} \nNetwork: {} \nHost: {} \n"
        "First address: {}\nLast address: {}\n").format(ip_class, num_networks, num_hosts, first_address, last_address)
    return [output, [ip_class, num_networks, num_hosts, first_address, last_address]]


def get_subnet_stats(ip_addr, subnet_mask):
    # input: eg. ("192.168.10.0","255.255.255.192")
    subnet_mask_binary = to_binary_string_list(subnet_mask)
    ip_class = get_ip_class(to_binary_string_list(ip_addr)[0])
    if ip_class not in ["A", "B", "C"]:
        raise Exception("Error: must be IP class A, B or C")

    # find the CIDR IP address by counting the number of 1 bits in the subnet mask
    subnet_mask_one_bit_count = 0
    for string in subnet_mask_binary:
        for char in string:
            if char == "1":
                subnet_mask_one_bit_count += 1
    cidr_ip_addr = ip_addr + "/" + str(subnet_mask_one_bit_count)

    # find the number of subnets: formula 2^x
    # class A: count 1 bits in last three bytes, class B: count 1 bits in last two
    # bytes, class C: count 1 bits in last byte
    class_to_index_d = {"A": 1, "B": 2, "C": 3}
    index = class_to_index_d[ip_class]
    x = sum([int(bit) for bit in "".join(subnet_mask_binary[index:]) if bit == "1"])
    num_subnets = 2 ** x

    # find the number of valid hosts
    # formula: (2^y - 2) where y is the number of unmasked bits
    y = 32 - subnet_mask_one_bit_count
    hosts_per_subnet = (2 ** y) - 2

    # find subnet addresses, broadcast addresses, first and last hosts
    ip_addr_binary = to_binary_string_list(ip_addr)
    index = class_to_index_d[ip_class]
    ip_addr_root = ip_addr_binary[:index]
    ip_addr_binary_root = "".join(ip_addr_root)

    max_num_bin = "1" * x
    if len(max_num_bin) < 1:
        max_num_bin = "0"
    max_num = int(max_num_bin, 2)

    valid_subnets = []
    broadcast_addresses = []
    first_host_addresses = []
    last_host_addresses = []

    for i in range(max_num + 1):
        # find the next subnet
        subnet_addr_binary_root = ip_addr_binary_root
        subnet_num_bin = bin(i)[2:]
        subnet_num_bin = "0" * (len(max_num_bin) - len(subnet_num_bin)) + subnet_num_bin
        subnet_addr_binary_root += subnet_num_bin
        end_num = "0" * (32 - len(subnet_addr_binary_root))
        subnet_addr_binary_root += end_num

        # find first host address
        first_host_address = subnet_addr_binary_root
        first_host_address = first_host_address[:-1]
        first_host_address += "1"
        first_host_address_binary_list = bin_string_to_bin_list(first_host_address)
        new_first_host_address = to_decimal_dot(first_host_address_binary_list)
        first_host_addresses.append(new_first_host_address)

        # find the next subnet (continued)
        new_subnet_binary_list = bin_string_to_bin_list(subnet_addr_binary_root)
        new_subnet_addr = to_decimal_dot(new_subnet_binary_list)
        valid_subnets.append(new_subnet_addr)

        # find the next broadcast address
        broadcast_addr_binary_root = ip_addr_binary_root
        broadcast_addr_binary_root += subnet_num_bin
        end_num = "1" * (32 - len(broadcast_addr_binary_root))
        broadcast_addr_binary_root += end_num

        # find the last host address
        last_host_address = broadcast_addr_binary_root
        last_host_address = last_host_address[:-1]
        last_host_address += "0"
        last_host_address_binary_list = bin_string_to_bin_list(last_host_address)
        new_last_host_address = to_decimal_dot(last_host_address_binary_list)
        last_host_addresses.append(new_last_host_address)

        # find the next broadcast address (continued)
        broadcast_num_binary_list = bin_string_to_bin_list(broadcast_addr_binary_root)
        new_broadcast_num = to_decimal_dot(broadcast_num_binary_list)
        broadcast_addresses.append(new_broadcast_num)

    # get output string for: valid_subnets, broadcast_addresses, first_host_addresses, last_host_addresses
    [valid_subnets_out_s, broadcast_addresses_out_s, first_host_addresses_out_s, last_host_addresses_out_s] = ["", "", "", ""]
    empty_output_strings = [valid_subnets_out_s, broadcast_addresses_out_s, first_host_addresses_out_s, last_host_addresses_out_s]
    address_categories = [valid_subnets, broadcast_addresses, first_host_addresses, last_host_addresses]
    output_strings = []

    for i in range(len(empty_output_strings)):
        current_output_string = empty_output_strings[i]
        current_address_category = address_categories[i]

        for j in range(len(current_address_category)):
            if j % 4 == 0:
                current_output_string += "\n"
            current_output_string += current_address_category[j] + ", "
        output_strings.append(current_output_string)
    [num_subnets_output, broadcast_addresses_output, first_host_addresses_output, last_host_addresses_output] = output_strings

    output = ("Address: {}\nSubnets: {}\n"
        "Addressable hosts per subnet: {}\n"
        "Valid subnets: {}\n"
        "Broadcast addresses: {}\n"
        "First addresses: {}\n"
        "Last addresses: {}\n").format(cidr_ip_addr, num_subnets, hosts_per_subnet, \
            num_subnets_output, broadcast_addresses_output, first_host_addresses_output, last_host_addresses_output)

    return [output, [cidr_ip_addr, num_subnets, hosts_per_subnet, \
        num_subnets_output, broadcast_addresses_output, first_host_addresses_output, last_host_addresses_output]]


def get_supernet_stats(ip_addr_list):
    # find the network mask
    ip_addr_list_binary = ["".join(to_binary_string_list(ip_addr)) for ip_addr in ip_addr_list]

    # find the length of the common prefix of the IP addresses and store it in the variable i
    i = 0
    bit_match = True
    # for each index in the first bit string
    while i < len(ip_addr_list_binary[0]) and bit_match:
        bit_i_in_first_num = ip_addr_list_binary[0][i]
        # loop through each IP address and check if every IP address has the same bit at index i as the first IP address
        for j in range(len(ip_addr_list_binary)):
            if ip_addr_list_binary[j][i] != bit_i_in_first_num:
                bit_match = False
                break
        if bit_match:
            i += 1

    cidr_ip_address = ip_addr_list[0] + "/" + str(i)

    network_mask_in_binary = ""
    for _ in range(i):
        network_mask_in_binary += "1"
    network_mask_in_binary += "0" * (32 - i)

    network_mask_binary_list = []
    i = 0
    for j in range(8, len(network_mask_in_binary) + 1, 8):
        network_mask_binary_list.append(network_mask_in_binary[i:j])
        i = j

    network_mask_decimal = to_decimal_dot(network_mask_binary_list)

    output = "Address: {} \nNetwork Mask: {}".format(cidr_ip_address, network_mask_decimal)
    return [output, [cidr_ip_address, network_mask_decimal]]
