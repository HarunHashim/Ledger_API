import { useState } from 'react'

function FindTransactions() {
  const [transactions, setTransactions] = useState([])
  const [id, setId] = useState('')
  const [message, setMessage] = useState('')
  const [hasSearched, setHasSearched] = useState(false)
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [totalElements, setTotalElements] = useState(0)
  const [type, setType] = useState('')
  const [status, setStatus] = useState('')
  const [minAmount, setMinAmount] = useState('')
  const [maxAmount, setMaxAmount] = useState('')
  const [dateFrom, setDateFrom] = useState('')
  const [dateTo, setDateTo] = useState('')
  const [activeFilters, setActiveFilters] = useState({
    type: false,
    status: false,
    minAmount: false,
    maxAmount: false,
    dateFrom: false,
    dateTo: false,
  })


async function handleFindTransactions(event) {
    event.preventDefault()

    if (minAmount && maxAmount && Number(maxAmount) < Number(minAmount)) {
      setTransactions([])
      setHasSearched(false)
      setMessage('Maximum amount must be greater than or equal to the minimum amount.')
      return
    }

    if (dateFrom && dateTo && dateTo < dateFrom) {
      setTransactions([])
      setHasSearched(false)
      setMessage('End date must be on or after the start date.')
      return
    }

    setPage(0)
    await fetchTransactions(id, 0)
  }

function toggleFilter(filterName) {
  const isActive = activeFilters[filterName]

  setActiveFilters({
    ...activeFilters,
    [filterName]: !isActive,
  })

  if (!isActive) {
    return
  }

  if (filterName === 'type') {
    setType('')
  }

  if (filterName === 'status') {
    setStatus('')
  }

  if (filterName === 'minAmount') {
    setMinAmount('')
  }

  if (filterName === 'maxAmount') {
    setMaxAmount('')
  }

  if (filterName === 'dateFrom') {
    setDateFrom('')
  }

  if (filterName === 'dateTo') {
    setDateTo('')
  }
}

async function fetchTransactions(walletId , pageNumber){
  
  try {
    // let url = `http://localhost:8080/api/transfers/${walletId}?page=${pageNumber}&size=10`
  
    //Check if type exists or is selected then add to request url (handles dynamic filtering)

    // Cleaner way to build url when it gets more and more complicated
    const params = new URLSearchParams({
      page: pageNumber,
      size: 10,
    })

    if (type) params.append('type', type)
    if (status) params.append('status', status)
    if (minAmount) params.append('minAmount', minAmount)
    if (maxAmount) params.append('maxAmount', maxAmount)
    if (dateFrom) params.append('dateFrom', dateFrom)
    if (dateTo) params.append('dateTo', dateTo)

    const url = 
       `http://localhost:8080/api/transfers/${walletId}?${params.toString()}`

    const response = await fetch(url)

    if (!response.ok) {
      const errorMessage = await response.text()
      throw new Error(
        errorMessage || 'Unable to retrieve transaction history'
      )
    }

    const data = await response.json()

    setTransactions(data.content)
    setTotalPages(data.totalPages)
    setTotalElements(data.totalElements)
    setHasSearched(true)

    setMessage(
      data.totalElements === 1
        ? '1 transaction found.'
        : `${data.totalElements} transactions found.`
    )

  } catch (error) {
    setTransactions([])
    setHasSearched(false)
    setMessage(error.message)
  }
}

function formatTransactionTime(transactionTime) {
  return new Date(transactionTime).toLocaleString()
}

function getTransactionRoute(transaction) {
  if (transaction.transactionType === 'DEPOSIT') {
    return `Deposit into wallet ${transaction.receiverId}`
  }

  if (transaction.transactionType === 'WITHDRAWAL') {
    return `Withdrawal from wallet ${transaction.senderId}`
  }

  return `Wallet ${transaction.senderId} to wallet ${transaction.receiverId}`
}

function handleNextPage() {
  const nextPage = page + 1

  setPage(nextPage)
  fetchTransactions(id, nextPage)
}

function handlePreviousPage() {
  const previousPage = page - 1

  setPage(previousPage)
  fetchTransactions(id, previousPage)
}
return (
  <section className="transactions-card">
    <form className="wallet-form-card" onSubmit={handleFindTransactions}>
      <h2 className="wallet-form-title">Transaction History</h2>
      <p className="wallet-form-copy">
        View every deposit, withdrawal, and transfer connected to a wallet.
      </p>

      <div className="transaction-filter-toolbar">
        <p className="transaction-filter-label">Add filters</p>
        <div className="transaction-filter-buttons">
          <button
            className={`filter-toggle-btn ${activeFilters.type ? 'is-active' : ''}`}
            type="button"
            onClick={() => toggleFilter('type')}
            aria-pressed={activeFilters.type}
          >
            Type
          </button>
          <button
            className={`filter-toggle-btn ${activeFilters.status ? 'is-active' : ''}`}
            type="button"
            onClick={() => toggleFilter('status')}
            aria-pressed={activeFilters.status}
          >
            Status
          </button>
          <button
            className={`filter-toggle-btn ${activeFilters.minAmount ? 'is-active' : ''}`}
            type="button"
            onClick={() => toggleFilter('minAmount')}
            aria-pressed={activeFilters.minAmount}
          >
            Minimum Amount
          </button>
          <button
            className={`filter-toggle-btn ${activeFilters.maxAmount ? 'is-active' : ''}`}
            type="button"
            onClick={() => toggleFilter('maxAmount')}
            aria-pressed={activeFilters.maxAmount}
          >
            Maximum Amount
          </button>
          <button
            className={`filter-toggle-btn ${activeFilters.dateFrom ? 'is-active' : ''}`}
            type="button"
            onClick={() => toggleFilter('dateFrom')}
            aria-pressed={activeFilters.dateFrom}
          >
            From Date
          </button>
          <button
            className={`filter-toggle-btn ${activeFilters.dateTo ? 'is-active' : ''}`}
            type="button"
            onClick={() => toggleFilter('dateTo')}
            aria-pressed={activeFilters.dateTo}
          >
            To Date
          </button>
        </div>
      </div>

      <div className="transaction-filter-fields">
        <label className="transaction-filter-field">
          <span>Wallet ID</span>
          <input
            type="number"
            placeholder="Wallet ID number"
            value={id}
            onChange={(event) => setId(event.target.value)}
          />
        </label>

        {activeFilters.type && (
          <label className="transaction-filter-field">
            <span>Transaction type</span>
            <select
              value={type}
              onChange={(event) => setType(event.target.value)}
            >
              <option value="">All transaction types</option>
              <option value="TRANSFER">Transfer</option>
              <option value="DEPOSIT">Deposit</option>
              <option value="WITHDRAWAL">Withdrawal</option>
            </select>
          </label>
        )}

        {activeFilters.status && (
          <label className="transaction-filter-field">
            <span>Transaction status</span>
            <select
              value={status}
              onChange={(event) => setStatus(event.target.value)}
            >
              <option value="">All statuses</option>
              <option value="SUCCESS">Success</option>
              <option value="FAILED">Failed</option>
            </select>
          </label>
        )}

        {activeFilters.minAmount && (
          <label className="transaction-filter-field">
            <span>Minimum amount</span>
            <input
              type="number"
              min="0"
              max={maxAmount || undefined}
              placeholder="Minimum amount"
              value={minAmount}
              onChange={(event) => setMinAmount(event.target.value)}
            />
          </label>
        )}

        {activeFilters.maxAmount && (
          <label className="transaction-filter-field">
            <span>Maximum amount</span>
            <input
              type="number"
              min={minAmount || '0'}
              placeholder="Maximum amount"
              value={maxAmount}
              onChange={(event) => setMaxAmount(event.target.value)}
            />
          </label>
        )}

        {activeFilters.dateFrom && (
          <label className="transaction-filter-field">
            <span>Start date</span>
            <input
              type="date"
              max={dateTo || undefined}
              value={dateFrom}
              onChange={(event) => setDateFrom(event.target.value)}
            />
          </label>
        )}

        {activeFilters.dateTo && (
          <label className="transaction-filter-field">
            <span>End date</span>
            <input
              type="date"
              min={dateFrom || undefined}
              value={dateTo}
              onChange={(event) => setDateTo(event.target.value)}
            />
          </label>
        )}
      </div>

      <button className="find-wallet-btn" type="submit">
        Find Transactions
      </button>

      {message && <p className="wallet-form-message">{message}</p>}
    </form>

    {transactions.length > 0 && (
      <div className="transactions-list">
        {transactions.map((transaction) => (
          <article className="transaction-item" key={transaction.tid}>
            <div className="transaction-item-header">
              <div>
                <p className="transaction-id">Transaction #{transaction.tid}</p>
                <h3>{getTransactionRoute(transaction)}</h3>
              </div>
              <span className={`transaction-type ${transaction.transactionType.toLowerCase()}`}>
                {transaction.transactionType}
              </span>
            </div>

            <div className="transaction-details">
              <p>
                <span>Amount</span>
                ${Number(transaction.transferAmount).toFixed(2)}
              </p>
              <p>
                <span>Status</span>
                {transaction.Transaction_status ?? transaction.transaction_status}
              </p>
              <p>
                <span>Completed</span>
                {formatTransactionTime(transaction.transactionTime)}
              </p>
            </div>
          </article>
        ))}
      </div>
    )}

    {hasSearched && totalPages > 0 && (
      <nav className="transaction-pagination" aria-label="Transaction pages">
        <p className="pagination-summary">
          Page {page + 1} of {totalPages} · {totalElements} transactions
        </p>

        <div className="pagination-controls">
          <button
            className="pagination-btn"
            type="button"
            onClick={handlePreviousPage}
            disabled={page === 0}
          >
            Previous
          </button>

          <span className="pagination-current">
            {page + 1} / {totalPages}
          </span>

          <button
            className="pagination-btn"
            type="button"
            onClick={handleNextPage}
            disabled={page >= totalPages - 1}
          >
            Next
          </button>
        </div>
      </nav>
    )}

    {hasSearched && transactions.length === 0 && (
      <div className="transactions-empty">
        No transactions are available for this wallet yet.
      </div>
    )}
  </section>
)
}

export default FindTransactions
