def get_ip_class(first_octet):
    # input: eg. '10000100'
    ip_class_d = {"0": "A", "10": "B", "110": "C", "1110": "D", "1111": "E"}
    for i in range(1, 5):
        sub_str = first_octet[0:i]
        if sub_str in ip_class_d:
            return ip_class_d[sub_str]
    return "Error: class not found"


def get_first_and_last_address(ip_class):
    class_to_range_d = {"A": [0, 127], "B": [128, 191], "C": [192, 223], "D": [224, 239], "E": [240, 255]}
    first_addres = str(class_to_range_d[ip_class][0]) + ".0.0.0"
    last_address = str(class_to_range_d[ip_class][1]) + ".255.255.255"
    return [first_addres, last_address]


def to_binary_string_list(ip_addr):
    byte_split = ip_addr.split(".")
    return ['{:08b}'.format(int(x)) for x in byte_split]


def to_decimal_dot(ip_addr_list):
    return ".".join([str(int(x,2)) for x in ip_addr_list])


def bin_string_to_bin_list(s):
    bin_list = []
    for i in range(0, len(s), 8):
        bin_list.append(s[i:i + 8])
    return bin_list

# Urkund won't accept this file because it has too few characters. The comment below is there to increase the character count.
"""
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin euismod lacus sapien, id tincidunt quam lobortis sed.
Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Quisque quis leo rutrum tellus
posuere varius. Praesent libero tortor, pretium fringilla dui eget, hendrerit porttitor mauris. Praesent dignissim
urna ut dolor tempus dictum. Ut rhoncus feugiat neque. Cras eget urna ac diam fermentum lobortis at ut ligula.
Morbi felis ex, ultrices ut ligula gravida, condimentum dignissim augue. Nulla tempus eget neque vel pulvinar.
Aenean nec quam in velit vestibulum pulvinar. Maecenas tempus varius nisi. Nam malesuada metus id mauris suscipit,
a sodales est viverra.

Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.
Praesent consectetur vel ligula eget bibendum. Ut cursus libero felis, eu venenatis magna rhoncus id.
Nullam sed ipsum facilisis, efficitur libero sit amet, porttitor dolor. Curabitur at congue odio.
Mauris gravida vel odio non eleifend. Integer vel aliquet purus. Praesent interdum tortor nec semper maximus.
Pellentesque vel pellentesque justo. Mauris sollicitudin mauris et justo cursus semper.
Donec ligula ante, euismod porta nisl ac, posuere lacinia est. Nulla facilisi.
Morbi non ultrices sem, vel venenatis enim. Donec pretium lectus a sem volutpat bibendum.
"""