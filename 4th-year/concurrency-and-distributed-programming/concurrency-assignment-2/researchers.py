import requests
import time
import json
from threading import Thread, Lock

from utils import get_server_of_type

'''
Program design:

1. A researcher thread starts and submits a proposal.
1.1. If the proposal is rejected, the researcher thread finishes.
2. If the proposal is accepted, the researcher accesses the account and adds his or her students to the account.
3. The researcher and students then withdraw money from the account and check account details.
4. Researcher or student threads stop when the account balance reaches zero or when the end date is passed.
'''

global_lock = Lock()


class Account:
    def __init__(self, file_name):
        self.file_name = file_name

    def get_account_info(self):
        account_info = requests.get(
            f'{get_server_of_type("college")}/account/{self.file_name}').json()
        return account_info

    def post_account_info(self, account_info):
        response = requests.post(
            f'{get_server_of_type("college")}/account/{self.file_name}', json=account_info)
        return response.json()
        
    def access_details(self, name):
        print(f'{name} is accessing the account details for account {self.file_name}')
        print('Account details:')
        account_info = self.get_account_info()
        acronym, title, description, authorized_researchers, days_left, balance, transactions = account_info.values()
        print(f'''\tAcronym: {acronym}\n\tTitle: {title}\n\tDescription: {description}\t
            Authorized researchers: {authorized_researchers}\n\tBalance: {balance}\n\tDays left {days_left}\n''')

    def list_transactions(self, name):
        print(f'{name} is accessing the transactions for the account {self.file_name}')
        account_info = self.get_account_info()
        transactions = account_info['transactions']
        print(f'Transactions for account {self.file_name}: {transactions}\n')

    def withdraw(self, name, amount):
        print(f'{name} attempting to withdraw {amount} from account {self.file_name}')
        account_info = self.get_account_info()
        balance = account_info['balance']
        if balance == 0:
            print(f'Account {self.file_name} is empty')
            return True
        if balance < amount:
            print(f'Withdrawal amount is greater than balance - setting amount to balance')
            amount = balance
        account_info['balance'] = balance - amount 
        account_info['transactions'] += [
            f'{name} successfully withdrew {amount} from account {self.file_name}']
        self.post_account_info(account_info)
        print(account_info['transactions'][-1] + '\n')
        return False

    def get_days_left(self):
        account_info = self.get_account_info()
        days_left = account_info['days_left']
        if days_left == 0:
            print(f'End date passed for account {self.file_name}')
            return 0
        account_info['days_left'] -= 1
        self.post_account_info(account_info)
        return days_left


# A project consists of a professor thread and several student threads
class Project:
    def __init__(self, prof_name, students, proposal):
        self.prof_name = prof_name
        self.students = students
        self.account_name = proposal['acronym']
        self.account = Account(self.account_name)
        self.threads = []
        prof_thread = Thread(target=self.setup_project, args=(proposal,))
        self.threads.append(prof_thread)
        print(f'Thread for professor {prof_name} started\n')
        prof_thread.start()

    def submit_proposal(self, proposal):
        title, amount = proposal['title'], proposal['amount']
        proposal['authorized_researchers'] = [self.prof_name]
        print(f'Professor {self.prof_name} is submitting a proposal with a title "{title}" for amount {amount}')
        print(
            f'Researcher {self.prof_name} is submitting a proposal with a title "{title}" for amount {amount}')
        response = requests.post(
            f'{get_server_of_type("agency")}/proposal', json=proposal)
        return response.json()

    def add_students_to_account(self):
        account_info = self.account.get_account_info()
        account_info['authorized_researchers'] += self.students
        self.account.post_account_info(account_info)
        print(f'{self.prof_name} added students {self.students} to account {self.account_name}\n')
        for student in self.students:
            student_thread = Thread(target=self.research, args=(student, 'student',))
            self.threads.append(student_thread)
            print(f'Thread for student {student} started\n')
            student_thread.start()
            time.sleep(1)

    def setup_project(self, proposal):
        response = self.submit_proposal(proposal)
        message, success = response['message'], response['success']
        print(f'{message}\n')
        if success: 
            self.add_students_to_account()
            self.research(self.prof_name, 'professor')

    def research(self, name, position):
        while True:
            with global_lock:
                days_left = self.account.get_days_left()
                if days_left == 0:
                    break
            with global_lock:
                account_empty = self.account.withdraw(name, 50000)
                if account_empty:
                    break
            with global_lock:
                self.account.access_details(name)
            with global_lock:
                self.account.list_transactions(name)
            time.sleep(1)
        print(f'Thread for {position} {name} for project {self.account_name} finished\n')



def main():
    with open('proposals.json') as file:
        proposals = json.load(file)['proposals']

    staff_names = {
        'John Smith': {
            'students': ['Hal Callaghan', 'Rebekah Blackmore']
        },
        'Alex Richardson': {
            'students': ['Eliot Padilla', 'Kathryn Hahn']
        },
        'Everett Park': {
            'students': ['Maira Garrett', 'Ben Cartwright']
        }
    }

    projects = []
    for i, prof_name in enumerate(staff_names.keys()):
        proposal = proposals[i]
        students = staff_names[prof_name]['students']
        project = Project(prof_name, students, proposal)
        projects.append(project)
        time.sleep(1)

    for project in projects:
        for thread in project.threads:
            thread.join()

    print('All threads finished')


if __name__ == '__main__':
    main()
