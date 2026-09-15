import { useState } from 'react'

function FindWallet() {
  
  const [foundWallet , setFoundWallet] = useState ('')
  const [message, setMessage] = useState('')
  const [id , setId] = useState('')
 

  async function handleFindWallet(event) {
    event.preventDefault()

    
    try {

        //URL here to change based on the API url to work incase hosted

        // Use of back quotationmarks to let js insert variable directly into strings . 
      
        const response = await fetch(`http://localhost:8080/api/wallets/${id}` );

        if (!response.ok) {
            throw new Error('Wallet not found ')
        }

        const data = await response.json()

        setFoundWallet(data)
        setMessage(`Wallet Found Successfully`)
      

    } catch (error) {
        setFoundWallet(null)
        setMessage(error.message)
    }
  }

  return (
    <form className="wallet-form-card" onSubmit={handleFindWallet}>
      
      <h2 className="wallet-form-title">Find Wallet</h2>
      <p className="wallet-form-copy">
        Use this to retrieve your wallet by placing in your ID.
      </p>
        <input
          type="number"
          placeholder="Wallet ID number"
          value={id}
          onChange={(event) => setId(event.target.value)}
        />

      <button className="find-wallet-btn" type="submit">Find Wallet</button>

      {message && <p className="wallet-form-message">{message}</p>}
      {foundWallet && (
        <div className="wallet-Found-summary">
          <h3>Wallet Found</h3>
          <p>ID: {foundWallet.id}</p>
          <p>Username: {foundWallet.userName}</p>
          <p>Balance: {foundWallet.balance}</p>
        </div>
      )}
    </form>
  )
}

export default FindWallet
