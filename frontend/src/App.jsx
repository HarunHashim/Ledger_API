import { useState } from 'react'
import './App.css'
import CreateWalletForm from './components/CreateWalletForm.jsx'

function App() {
  const [showForm, setShowForm] = useState(false)

  return (
    <div className="app-container">
      {/* Navigation */}
      <nav className="navbar">
        <div className="nav-content">
          <div className="logo">💰 Centaur Ledger</div>
          <button className="nav-btn" onClick={() => setShowForm(!showForm)}>
            {showForm ? 'Close' : 'Get Started'}
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
          <button 
            className="cta-button"
            onClick={() => setShowForm(!showForm)}
          >
            {showForm ? 'Hide Form' : 'Create Your Wallet'}
          </button>
        </div>
      </section>

      {/* Main Content */}
      <main className="main-content">
        <div className="container">
          {showForm && (
            <div className="form-section">
              <h2>Create Your Wallet</h2>
              <CreateWalletForm />
            </div>
          )}

          {/* Features Section */}
          <section className="features">
            <h2>Why Choose Centaur Ledger?</h2>
            <div className="features-grid">
              <div className="feature-card">
                <div className="feature-icon">📊</div>
                <h3>Track Transactions</h3>
                <p>Keep detailed records of all your financial transactions</p>
              </div>
              <div className="feature-card">
                <div className="feature-icon">🔒</div>
                <h3>Secure & Private</h3>
                <p>Your financial data is encrypted and protected</p>
              </div>
              <div className="feature-card">
                <div className="feature-icon">⚡</div>
                <h3>Lightning Fast</h3>
                <p>Built for performance with instant updates</p>
              </div>
              <div className="feature-card">
                <div className="feature-icon">📱</div>
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
