import requests
import random

BASE_URL = "http://localhost:8080"


def create_wallet(name, balance):
    response = requests.post(
        f"{BASE_URL}/api/wallets",
        json={
            "userName": name,
            "balance": balance
        }
    )

    response.raise_for_status()
    return response.json()

def deposit(wallet_id, amount):
    response = requests.post(
        f"{BASE_URL}/api/wallets/{wallet_id}/deposit",
        json={"amount": amount}
    )
    response.raise_for_status()


def withdraw(wallet_id, amount):
    response = requests.post(
        f"{BASE_URL}/api/wallets/{wallet_id}/withdraw",
        json={"amount": amount}
    )
    response.raise_for_status()


def transfer(sender_id, receiver_id, amount):
    response = requests.post(
        f"{BASE_URL}/api/transfers",
        json={
            "senderId": sender_id,
            "receiverId": receiver_id,
            "transferAmount": amount
        }
    )
    response.raise_for_status()


def main():

    # ---------- Create demo wallets ----------

    maya = create_wallet("Maya", 1500)
    alex = create_wallet("Alex", 850)
    harun = create_wallet("Harun", 2200)
    nina = create_wallet("Nina", 975)
    daniel = create_wallet("Daniel", 3100)
    sarah = create_wallet("Sarah", 1250)
    james = create_wallet("James", 780)
    amina = create_wallet("Amina", 4600)

    wallets = [
        maya, alex, harun, nina,
        daniel, sarah, james, amina
    ]

    print(f"Created {len(wallets)} wallets.")

    # ---------- Deposits ----------

    deposit(maya["id"], 500)
    deposit(alex["id"], 250)
    deposit(harun["id"], 1000)
    deposit(nina["id"], 300)
    deposit(amina["id"], 750)

    # ---------- Withdrawals ----------

    withdraw(maya["id"], 125)
    withdraw(harun["id"], 300)
    withdraw(daniel["id"], 450)
    withdraw(sarah["id"], 100)

    # ---------- Transfers ----------

    transfer(maya["id"], alex["id"], 200)
    transfer(alex["id"], nina["id"], 100)
    transfer(harun["id"], maya["id"], 350)
    transfer(daniel["id"], sarah["id"], 500)
    transfer(amina["id"], james["id"], 275)
    transfer(nina["id"], harun["id"], 150)
    transfer(sarah["id"], amina["id"], 225)
    transfer(james["id"], maya["id"], 75)
    
    # Random deposits
    for _ in range(20):
        wallet = random.choice(wallets)
        amount = random.randint(25, 500)

        deposit(wallet["id"], amount)


    # Random withdrawals
    for _ in range(20):
        wallet = random.choice(wallets)
        amount = random.randint(10, 150)

        try:
            withdraw(wallet["id"], amount)
        except requests.HTTPError:
            # Skip if wallet doesn't have enough money
            continue


    # Random transfers
    for _ in range(40):
        sender, receiver = random.sample(wallets, 2)
        amount = random.randint(10, 200)

        try:
            transfer(
                sender["id"],
                receiver["id"],
                amount
            )
        except requests.HTTPError:
            # Skip insufficient-funds transfers
            continue

    print("Demo transactions created.")
    print("Centaur Ledger demo data ready.")


if __name__ == "__main__":
    main()
    
    
    
# FOR FUTURE NOTE
# You can access SQL directly inside the shell just by using :
#                 
#           :  docker compose exec postgres psql -U postgres -d Ledger_API 
# exit with : 
#               \q
