import { useState } from 'react'

function TransferMoneyForm() {
  const [senderId, setSenderId] = useState('')
  const [receiverId, setReceiverId] = useState('')
  const [transferAmount, setTransferAmount] = useState('')
  const [message, setMessage] = useState('')
  const [transaction, setTransaction] = useState(null)

  async function handleTransfer(event) {
    event.preventDefault()

    try {
      const response = await fetch('http://localhost:8080/api/transfers', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          senderId: Number(senderId),
          receiverId: Number(receiverId),
          transferAmount: Number(transferAmount),
        }),
      })

      if (!response.ok) {
        const errorMessage = await response.text()
        throw new Error(errorMessage || 'Transfer failed')
      }

      const data = await response.json()

      setTransaction(data)
      setMessage(`Transfer successful. Transaction ID: ${data.tid}`)
      setSenderId('')
      setReceiverId('')
      setTransferAmount('')
    } catch (error) {
      setTransaction(null)
      setMessage(error.message)
    }
  }

  return (
    <form className="wallet-form-card" onSubmit={handleTransfer}>
      <h2 className="wallet-form-title">Transfer Money</h2>
      <p className="wallet-form-copy">
        Move money between two wallets by entering the sender, receiver, and transfer amount.
      </p>

      <div className="wallet-form-fields">
        <div className="transfer-route-row">
          <input
            type="number"
            placeholder="Sender Wallet ID"
            value={senderId}
            onChange={(event) => setSenderId(event.target.value)}
          />

          <div className="transfer-route-arrow" aria-hidden="true">
            <span className="transfer-route-line" />
            <span className="transfer-route-icon" />
          </div>

          <input
            type="number"
            placeholder="Receiver Wallet ID"
            value={receiverId}
            onChange={(event) => setReceiverId(event.target.value)}
          />
        </div>

        <input
          type="number"
          placeholder="Transfer Amount"
          value={transferAmount}
          onChange={(event) => setTransferAmount(event.target.value)}
        />
      </div>

      <button className="transfer-money-btn" type="submit">Transfer Money</button>

      {message && <p className="wallet-form-message">{message}</p>}
      {transaction && (
        <div className="wallet-created-summary">
          <h3>Transaction Complete</h3>
          <p>Transaction ID: {transaction.tid}</p>
          <p>Sender ID: {transaction.senderId}</p>
          <p>Receiver ID: {transaction.receiverId}</p>
          <p>Amount: {transaction.transferAmount}</p>
          <p>Time: {transaction.transactionTime}</p>
          <p>Type: {transaction.transactionType}</p>
          <p>Status: {transaction.transaction_status}</p>
        </div>
      )}
    </form>
  )
}

export default TransferMoneyForm
