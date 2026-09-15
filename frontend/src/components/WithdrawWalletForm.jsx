import { useState } from 'react'

function WithdrawWalletForm() {
  const [id, setId] = useState('')
  const [amount, setAmount] = useState('')
  const [message, setMessage] = useState('')
  const [updatedWallet, setUpdatedWallet] = useState(null)

  async function handleWithdraw(event) {
    event.preventDefault()

    try {
      const response = await fetch(`http://localhost:8080/api/wallets/${id}/withdraw`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          amount: Number(amount),
        }),
      })

      if (!response.ok) {
        const errorMessage = await response.text()
        throw new Error(errorMessage || 'Withdrawal failed')
      }

      const data = await response.json()

      setUpdatedWallet(data)
      setMessage(`Withdrawal successful for wallet ID: ${data.id}`)
      setId('')
      setAmount('')
    } catch (error) {
      setUpdatedWallet(null)
      setMessage(error.message)
    }
  }

  return (
    <form className="wallet-form-card" onSubmit={handleWithdraw}>
      <h2 className="wallet-form-title">Withdraw Funds</h2>
      <p className="wallet-form-copy">
        Remove money from an existing wallet by entering the wallet ID and withdrawal amount.
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
          placeholder="Withdrawal Amount"
          value={amount}
          onChange={(event) => setAmount(event.target.value)}
        />
      </div>

      <button className="withdraw-wallet-btn" type="submit">Withdraw Funds</button>

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

export default WithdrawWalletForm
