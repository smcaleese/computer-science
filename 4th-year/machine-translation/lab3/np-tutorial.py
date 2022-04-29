import numpy as np

def main():
    arr1 = np.array([1, 2, 3]) # [1, 2, 3]
    arr2 = np.array(20) # 20
    print(arr1)
    print(arr2)

    # reshape array
    one_to_nine = np.arange(9)
    one_to_nine_grid = one_to_nine.reshape(3, 3)
    print(one_to_nine_grid)

if __name__ == '__main__':
    main()
