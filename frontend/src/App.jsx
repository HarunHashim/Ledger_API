import { useState } from 'react'
import './App.css'
import CreateWalletForm from './components/CreateWalletForm.jsx'
import DepositWalletForm from './components/DepositWalletForm.jsx'
import FindTransactions from './components/FindTransactions.jsx'
import FindWallet from './components/FindWallet.jsx'
import TransferMoneyForm from './components/TransferMoneyForm.jsx'
import WithdrawWalletForm from './components/WithdrawWalletForm.jsx'

function App() {
  const [activePanel, setActivePanel] = useState(null)

  function togglePanel(panelName) {
    setActivePanel((current) => (current === panelName ? null : panelName))
  }

  return (
    <div className="app-container">
      {/* Navigation */}
      <nav className="navbar">
        <div className="nav-content">
          <div className="logo">{'\u{1F4B0}'} Centaur Ledger</div>
          <button className="nav-btn" onClick={() => togglePanel('create')}>
            {activePanel === 'create' ? 'Close' : 'Get Started'}
          </button>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="hero">
        <div className="hero-content">
          <h1 className="hero-title">Your Personal Financial Ledger</h1>
          <p className="hero-subtitle">
            Simple, elegant, and powerful wallet management for modern finance
          </p>
          <div className="hero-actions">
            <div className="hero-actions-primary">
              <button
                className="cta-button"
                onClick={() => togglePanel('create')}
              >
                {activePanel === 'create' ? 'Hide Create Wallet' : 'Create Your Wallet'}
              </button>
              <button
                className="secondary-cta-button"
                onClick={() => togglePanel('find')}
              >
                {activePanel === 'find' ? 'Hide Find Wallet' : 'Find Wallet'}
              </button>
            </div>
            <div className="hero-actions-secondary">
              <button
                className="action-chip"
                onClick={() => togglePanel('deposit')}
              >
                {activePanel === 'deposit' ? 'Hide Deposit' : 'Deposit Funds'}
              </button>
              <button
                className="action-chip"
                onClick={() => togglePanel('withdraw')}
              >
                {activePanel === 'withdraw' ? 'Hide Withdrawal' : 'Withdraw Funds'}
              </button>
              <button
                className="action-chip"
                onClick={() => togglePanel('transfer')}
              >
                {activePanel === 'transfer' ? 'Hide Transfer' : 'Transfer Money'}
              </button>
              <button
                className="action-chip"
                onClick={() => togglePanel('transactions')}
              >
                {activePanel === 'transactions' ? 'Hide History' : 'Transaction History'}
              </button>
            </div>
          </div>
        </div>
      </section>

      {/* Main Content */}
      <main className="main-content">
        <div className="container">
          {activePanel === 'create' && (
            <div className="form-section">
              <div className="wallet-actions-header">
                <p className="wallet-actions-kicker">Wallet Actions</p>
                <h2>Create a New Wallet</h2>
                <p className="wallet-actions-copy">
                  Set up a wallet and test your create-wallet flow.
                </p>
              </div>
              <CreateWalletForm />
            </div>
          )}

          {activePanel === 'find' && (
            <div className="form-section">
              <div className="wallet-actions-header">
                <p className="wallet-actions-kicker">Wallet Actions</p>
                <h2>Find a Wallet</h2>
                <p className="wallet-actions-copy">
                  Look up a wallet by ID and check its current details instantly.
                </p>
              </div>
              <FindWallet />
            </div>
          )}

          {activePanel === 'deposit' && (
            <div className="form-section">
              <div className="wallet-actions-header">
                <p className="wallet-actions-kicker">Wallet Actions</p>
                <h2>Deposit Into a Wallet</h2>
                <p className="wallet-actions-copy">
                  Add funds to an existing wallet using its ID and the amount you want to deposit.
                </p>
              </div>
              <DepositWalletForm />
            </div>
          )}

          {activePanel === 'withdraw' && (
            <div className="form-section">
              <div className="wallet-actions-header">
                <p className="wallet-actions-kicker">Wallet Actions</p>
                <h2>Withdraw From a Wallet</h2>
                <p className="wallet-actions-copy">
                  Remove funds from a wallet and verify the updated balance right away.
                </p>
              </div>
              <WithdrawWalletForm />
            </div>
          )}

          {activePanel === 'transfer' && (
            <div className="form-section">
              <div className="wallet-actions-header">
                <p className="wallet-actions-kicker">Wallet Actions</p>
                <h2>Transfer Between Wallets</h2>
                <p className="wallet-actions-copy">
                  Move money from one wallet to another and review the transaction details after submission.
                </p>
              </div>
              <TransferMoneyForm />
            </div>
          )}

          {activePanel === 'transactions' && (
            <div className="form-section">
              <div className="wallet-actions-header">
                <p className="wallet-actions-kicker">Wallet Activity</p>
                <h2>View Transaction History</h2>
                <p className="wallet-actions-copy">
                  Review every deposit, withdrawal, and transfer for a wallet.
                </p>
              </div>
              <FindTransactions />
            </div>
          )}

          {/* Features Section */}
          <section className="features">
            <h2>Why Choose Centaur Ledger?</h2>
            <div className="features-grid">
              <div className="feature-card">
                <div className="feature-icon">{'\u{1F4CA}'}</div>
                <h3>Track Transactions</h3>
                <p>Keep detailed records of all your financial transactions</p>
              </div>
              <div className="feature-card">
                <div className="feature-icon">{'\u{1F512}'}</div>
                <h3>Secure & Private</h3>
                <p>Your financial data is encrypted and protected</p>
              </div>
              <div className="feature-card">
                <div className="feature-icon">{'\u{26A1}'}</div>
                <h3>Lightning Fast</h3>
                <p>Built for performance with instant updates</p>
              </div>
              <div className="feature-card">
                <div className="feature-icon">{'\u{1F4F1}'}</div>
                <h3>Always Accessible</h3>
                <p>Access your ledger anytime, anywhere</p>
              </div>
            </div>
          </section>
        </div>
      </main>

      {/* Footer */}
      <footer className="footer">
        <p>&copy; 2024 Centaur Ledger. All rights reserved.</p>
      </footer>
    </div>
  )
}

export default App
