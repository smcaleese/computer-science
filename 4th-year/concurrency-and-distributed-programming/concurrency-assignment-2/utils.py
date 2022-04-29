from models import Service
import requests


service_registry_endpoint = 'http://localhost:9000'


def register_with_registry(service: Service):
    try:
        response = requests.post(
            f'{service_registry_endpoint}/register', json=service.dict())
        if response.status_code == 200:
            return True
    except Exception as e:
        return False


def unregister_with_registry(service: Service):
    try:
        response = requests.post(
            f'{service_registry_endpoint}/unregister', json=service.dict())
        if response.status_code == 200:
            return True
    except Exception as e:
        return False


def get_server_of_type(service_type):
    response = requests.get(
        f'{service_registry_endpoint}/service/{service_type}').json()
    return f"http://{response['url']}:{response['port']}"
