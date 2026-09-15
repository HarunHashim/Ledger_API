import { useState } from 'react'

function CreateWalletForm() {
  const [userName, setUserName] = useState('')
  const [balance, setBalance] = useState('')
  const [message, setMessage] = useState('')
  const [createdWallet, setCreatedWallet] = useState(null)

  async function handleCreateWallet(event) {
    event.preventDefault()

    try {
        //URL here to change based on the API url to work incase hosted

      const response = await fetch('http://localhost:8080/api/wallets', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          userName: userName,
          balance: Number(balance),
        }),
      })

      if (!response.ok) {
        throw new Error('Failed to create wallet')
      }

      const data = await response.json()

      setCreatedWallet(data)
      setMessage(`Wallet created successfully. ID: ${data.id}`)
      setUserName('')
      setBalance('')

    } catch (error) {
      setMessage(error.message)
    }
  }

  return (
    <form className="wallet-form-card" onSubmit={handleCreateWallet}>
      <p className="wallet-form-kicker">Wallet Setup</p>
      <h2 className="wallet-form-title">Create Wallet</h2>
      <p className="wallet-form-copy">
        Use this quick form to test your new wallet creation flow.
      </p>

      <div className="wallet-form-fields">
        <input
          type="text"
          placeholder="Username"
          value={userName}
          onChange={(event) => setUserName(event.target.value)}
        />

        <input
          type="number"
          placeholder="Initial Balance"
          value={balance}
          onChange={(event) => setBalance(event.target.value)}
        />
      </div>

      <button className="create-wallet-btn" type="submit">Create Wallet</button>

      {message && <p className="wallet-form-message">{message}</p>}
      {createdWallet && (
        <div className="wallet-created-summary">
          <h3>Wallet Created</h3>
          <p>ID: {createdWallet.id}</p>
          <p>Username: {createdWallet.userName}</p>
          <p>Balance: {createdWallet.balance}</p>
        </div>
      )}
    </form>
  )
}

export default CreateWalletForm
