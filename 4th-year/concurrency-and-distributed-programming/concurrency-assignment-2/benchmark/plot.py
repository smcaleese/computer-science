import matplotlib.pyplot as plt 
import pandas

if __name__ == '__main__':
    df = pandas.read_csv('benchmark.csv')
    data = df.to_numpy()

    x_values = [x[0] for x in data]
    y_values = [x[1] for x in data]

    plt.plot(x_values, y_values)
    plt.xlabel('Percentage of requests (%)')
    plt.ylabel('Time (seconds)')
    plt.savefig('benchmark.png')
    plt.close()
