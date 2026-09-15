import { useState } from 'react'

function DepositWalletForm() {
  const [id, setId] = useState('')
  const [amount, setAmount] = useState('')
  const [message, setMessage] = useState('')
  const [updatedWallet, setUpdatedWallet] = useState(null)

  async function handleDeposit(event) {
    event.preventDefault()

    try {
      const response = await fetch(`http://localhost:8080/api/wallets/${id}/deposit`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          amount: Number(amount),
        }),
      })

      if (!response.ok) {
        
        //Make this a habit since backend is configured to return error message for now 
        //For production these messages will be logged onto a separate logging file or api to keep actual page clean and debugging cleaner.

        const errorMessage = await response.text()
        throw new Error(errorMessage || 'Deposit failed')
      }

      const data = await response.json()

      setUpdatedWallet(data)
      setMessage(`Deposit successful for wallet ID: ${data.id}`)
      setId('')
      setAmount('')

    } catch (error) {
      setUpdatedWallet(null)
      setMessage(error.message)
    }
  }

  return (
    <form className="wallet-form-card" onSubmit={handleDeposit}>
      <h2 className="wallet-form-title">Deposit Funds</h2>
      <p className="wallet-form-copy">
        Add money to an existing wallet by entering the wallet ID and deposit amount.
      </p>

      <div className="wallet-form-fields">
        <input
          type="number"
          placeholder="Wallet ID number"
          value={id}
          onChange={(event) => setId(event.target.value)}
        />

        <input
          type="number"
          placeholder="Deposit Amount"
          value={amount}
          onChange={(event) => setAmount(event.target.value)}
        />
      </div>

      <button className="deposit-wallet-btn" type="submit">Deposit Funds</button>

      {message && <p className="wallet-form-message">{message}</p>}
      {updatedWallet && (
        <div className="wallet-created-summary">
          <h3>Wallet Updated</h3>
          <p>ID: {updatedWallet.id}</p>
          <p>Username: {updatedWallet.userName}</p>
          <p>Balance: {updatedWallet.balance}</p>
        </div>
      )}
    </form>
  )
}

export default DepositWalletForm
