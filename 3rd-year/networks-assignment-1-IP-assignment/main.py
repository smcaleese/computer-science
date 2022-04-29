import tkinter as tk
import tkinter.scrolledtext as st
from ip_calculator import get_class_stats, get_subnet_stats, get_supernet_stats

window = tk.Tk()
window.resizable(False, True)

frame1 = tk.Frame(master=window, height=300, width=600, bg="white")
frame1.pack(fill=tk.X)

frame11 = tk.Frame(master=frame1, height=300, width=300, bg="red")
frame11.pack(fill=tk.BOTH, side=tk.LEFT, expand=True)
frame12 = tk.Frame(master=frame1, height=300, width=300, bg="green")
frame12.pack(fill=tk.BOTH, side=tk.RIGHT, expand=True)

entry1 = tk.Entry(master=frame12)
entry2 = tk.Entry(master=frame12)
entry3 = tk.Entry(master=frame12)

label1 = tk.Label(master=frame11, text="IP Address:")
label2 = tk.Label(master=frame11, text="Subnet Mask:")
label3 = tk.Label(master=frame11, text="IP Address List (numbers seperated by commas):")

label1.pack(pady=20)
label2.pack(pady=20)
label3.pack(pady=20)

entry1.pack(pady=20)
entry2.pack(pady=20)
entry3.pack(pady=20)

frame3 = None
# deletes and recreates frame3 to clear the screen
def recreate_frame_3():
    global frame3
    frame3.destroy()
    frame3 = tk.Frame(master=window, height=100, width=600, bg="white")
    frame3.pack(fill=tk.X)


def output_class_stats():
    recreate_frame_3()
    ip_addr = entry1.get()
    class_stats = get_class_stats(ip_addr)[0]
    output_label = tk.Label(master=frame3, text=class_stats, bg="white")
    output_label.pack()
    print(class_stats)


def output_subnet_stats():
    recreate_frame_3()
    ip_addr = entry1.get()
    subnet_mask = entry2.get()
    subnet_stats = get_subnet_stats(ip_addr, subnet_mask)[0]

    text_area = st.ScrolledText(frame3, width=80, height=10)
    text_area.grid(column = 0, pady = 10, padx = 10)
    text_area.insert(tk.INSERT, subnet_stats)
    # Make it read-only
    text_area.configure(state ='disabled')
    print(subnet_stats)


def output_supernet_stats():
    recreate_frame_3()
    ip_addr_list = entry3.get().split(",")

    print(ip_addr_list, ip_addr_list[0])
    supernet_stats = get_supernet_stats(ip_addr_list)[0]
    output_label = tk.Label(master=frame3, text=supernet_stats, bg="white")
    output_label.pack()
    print(supernet_stats)


frame2 = tk.Frame(master=window, height=100, width=600, bg="yellow")
frame2.pack(fill=tk.X)

frame3 = tk.Frame(master=window, height=100, width=600, bg="white")
frame3.pack(fill=tk.X)

get_class_stats_button = tk.Button(frame2, text="Get Class Stats", command=output_class_stats)
get_subnet_stats_button = tk.Button(frame2, text="Get Subnet Stats", command=output_subnet_stats)
get_supernet_stats_button = tk.Button(frame2, text="Get Supernet Stats", command=output_supernet_stats)

get_class_stats_button.grid(row=1, column=2, padx=25, pady=20)
get_subnet_stats_button.grid(row=1, column=4, padx=25, pady=20)
get_supernet_stats_button.grid(row=1, column=6, padx=25, pady=20)

window.mainloop()