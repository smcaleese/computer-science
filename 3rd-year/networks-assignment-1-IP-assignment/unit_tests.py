import unittest
from ip_calculator import get_class_stats, get_subnet_stats, get_supernet_stats

class Test(unittest.TestCase):

    # test get_class_stats
    def test_get_class_stats_class_a(self):
        actual = get_class_stats("50.25.18.7")[1]
        expected = ["A", 128, 16777216, "0.0.0.0", "127.255.255.255"]
        self.assertEqual(actual, expected)


    def test_get_class_stats_class_b(self):
        actual = get_class_stats("136.206.18.7")[1]
        expected = ["B", 16384, 65536, "128.0.0.0", "191.255.255.255"]
        self.assertEqual(actual, expected)


    def test_get_class_stats_class_c(self):
        actual = get_class_stats("192.8.6.5")[1]
        expected = ["C", 2097152, 256, "192.0.0.0", "223.255.255.255"]
        self.assertEqual(actual, expected)


    def test_get_class_stats_class_d(self):
        actual = get_class_stats("230.77.22.5")[1]
        expected = ["D", "N/A", "N/A", "224.0.0.0", "239.255.255.255"]
        self.assertEqual(actual, expected)


    def test_get_class_stats_class_e(self):
        actual = get_class_stats("249.15.56.10")[1]
        expected = ["E", "N/A", "N/A", "240.0.0.0", "255.255.255.255"]
        self.assertEqual(actual, expected)

    # test get_subnet_stats
    def test_get_subnet_stats_class_b(self):
        actual = get_subnet_stats("172.16.0.0","255.255.192.0")[1]
        expected = ["172.16.0.0/18", 4, 16382,
            ["172.16.0.0", "172.16.64.0", "172.16.128.0", "172.16.192.0"],
            ["172.16.63.255","172.16.127.255","172.16.191.255","172.16.255.255"],
            ["172.16.0.1","172.16.64.1","172.16.128.1","172.16.192.1"],
            ["172.16.63.254","172.16.127.254","172.16.191.254","172.16.255.254"]]
        self.assertEqual(actual, expected)

    def test_get_subnet_stats_class_c(self):
        actual = get_subnet_stats("192.168.10.0","255.255.255.192")[1]
        expected = ["192.168.10.0/26", 4, 62,
            ["192.168.10.0", "192.168.10.64", "192.168.10.128", "192.168.10.192"],
            ["192.168.10.63","192.168.10.127","192.168.10.191","192.168.10.255"],
            ["192.168.10.1","192.168.10.65","192.168.10.129","192.168.10.193"],
            ["192.168.10.62","192.168.10.126","192.168.10.190","192.168.10.254"]]
        self.assertEqual(actual, expected)

    # test get_supernet_stats
    def test_get_supernet_stats_class_c(self):
        actual = get_supernet_stats(["205.100.0.0","205.100.1.0","205.100.2.0","205.100.3.0"])[1]
        expected = ["205.100.0.0/22", "255.255.252.0"]
        self.assertEqual(actual, expected)

unittest.main()