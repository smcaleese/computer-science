def create_grid(input_string):
    grid = []
    new_row = []
    for i, char in enumerate(input_string):
        if i % 3 == 0 and i > 0:
            grid.append(new_row)
            new_row = []
        new_row.append(char)
    grid.append(new_row)
    return grid

def find_coord(num, grid):
    for y in range(len(grid[0])):
        for x in range(len(grid)):
            if grid[y][x] == num:
                return [y, x]

def find_distance(coord_1, coord_2):
    y_diff = abs(int(coord_2[0]) - int(coord_1[0]))
    x_diff = abs(int(coord_2[1]) - int(coord_1[1]))
    return y_diff + x_diff

