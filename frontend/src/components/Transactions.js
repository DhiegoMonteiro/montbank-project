import '../css/forms.css';
import { useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';

function Transactions({ onClose }) {
  const [transactions, setTransactions] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [currentTransaction, setCurrentTransaction] = useState(null);

  const [newTransaction, setnewTransaction] = useState({
    amount: '',
    receiver: '',
    title: '',
    senderName: '',
    senderEmail: '',
    createdAt: '',
    transactionId: ''
  });

  const [formData, setFormData] = useState({
    balance: ''
  });
  
  const decoded = jwtDecode(localStorage.getItem('authToken'));
  const userEmail = decoded.email;

  const fetchTransactions = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await fetch('http://localhost:8081/api/transactions/history', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
        }
      });
      if (!response.ok) {
        throw new Error('Não foi possível carregar suas transações, verifique se está logado.');
      }
      const data = await response.json();
      setTransactions(data);
    } catch (err) {
      setError("Não foi possivel listar suas transações, por favor, tente novamente mais tarde.");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchTransactions();
    fetchBalance();
  }, []);

  const handlenewTransactionChange = (e) => {
    const { name, value } = e.target;
    setnewTransaction(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const formatDateTime = (isoString) => {
    const date = new Date(isoString);
    return date.toLocaleString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

    const fetchBalance = async () => {
      setIsLoading(true);
      setError(null);
      try {
        const response = await fetch('http://localhost:8081/api/auth/user/profile', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
          }
        });

        if (!response.ok) {
          throw new Error('Não foi possível acessar seu saldo, verifique se está logado.');
        }

        const data = await response.json();
        setFormData({
          balance: data.balance
        });
      } catch (err) {
        setError(err.message);
        setTimeout(() => setError(null), 2000);
      } finally {
        setIsLoading(false);
      }
    };



  const handleNewTransaction = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);
    try {
      const response = await fetch('http://localhost:8081/api/transactions/history/new', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`
        },
        body: JSON.stringify(newTransaction)
      });
      if (!response.ok) {
        const data = await response.json();
        throw new Error(data.message || 'Erro ao efetuar transação');
      }
      setShowCreateModal(false);
      setnewTransaction({ amount: '', receiver: '', title: '' });
      await fetchTransactions();
      await fetchBalance();
    } catch (err) {
      setError(err.message);
      setTimeout(() => setError(null), 2000);
    } finally {
      setIsLoading(false);
    }
  };

  const openEditModal = (transaction) => {
    setCurrentTransaction(transaction);
    setShowEditModal(true);
    setShowCreateModal(false);
  };

  const handleEditChange = (e) => {
    const { name, value } = e.target;
    setCurrentTransaction(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleEditTransaction = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);
    try {
      const response = await fetch(`http://localhost:8081/api/transactions/history/${currentTransaction.transactionId}/edit`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`
        },
        body: JSON.stringify({
          title: currentTransaction.title
        })
      });
      if (!response.ok) {
        const data = await response.json();
        throw new Error(data.message || 'Erro ao editar título da transação, apenas quem a enviou pode editar.');
      }
      setShowEditModal(false);
      setCurrentTransaction(null);
      fetchTransactions();
    } catch (err) {
      setError(err.message);
      setTimeout(() => setError(null), 2000);
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeleteTransaction = async (transactionId) => {
    if (!window.confirm('Tem certeza que deseja deletar o histórico de transação? esta operação não retornará o dinheiro e será irreversível.')) return;

    setIsLoading(true);
    setError(null);
    try {
      const response = await fetch(`http://localhost:8081/api/transactions/history/${transactionId}/delete`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`
        }
      });
      if (!response.ok) {
        const data = await response.json();
        throw new Error(data.message || 'Erro ao deletar transação, apenas quem a enviou pode fazer a deleção.');
      }
      fetchTransactions();
    } catch (err) {
      setError(err.message);
      setTimeout(() => setError(null), 2000);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className='wrapperForm'>
      <span className="closeButton" onClick={onClose}>&times;</span>
      <div className='TransactionForm'>
        <h2>Minhas Transações</h2>
        <p className='saldo'><strong>Saldo: {formData.balance},00</strong></p>

        {isLoading && <div className="loading-message">Carregando...</div>}
        {error && <div className="error-message">{error}</div>}
        <div className='generateTransactionButton'>
          <button onClick={() => { setShowCreateModal(true); setShowEditModal(false); }}>Efetuar nova transação</button>
        </div>

        {transactions.length === 0 ? (
          <div>
            <p>Nenhum transação efetuada.</p>
          </div>
        ) : (
          <div className='transactionsList'>
           <ul>
    {transactions.map((transaction) => {
      const isSent = transaction.senderEmail === userEmail;
      return (
        <li key={transaction.transactionId} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '8px 0' }}>
          <div>
            <strong>{transaction.title} - {transaction.receiver} </strong> - 
            <span style={{ color: isSent ? 'red' : 'green', marginLeft: '5px' }}>
              {isSent ? 'Enviada' : 'Recebida'}
            </span>
          </div>
          <div>
            <button className='listButton' onClick={() => openEditModal(transaction)}>Visualizar</button>
            <button className='listButton' onClick={() => handleDeleteTransaction(transaction.transactionId)}>Deletar</button>
          </div>
        </li>
      );
    })}
  </ul>
          </div>
        )}


        {showCreateModal && (
          <div className="modal">
            <span className="closeButton" onClick={() => setShowCreateModal(false)}>&times;</span>
            <div className="modal-content">
              <h3>Efetuar nova transação</h3>
              <form onSubmit={handleNewTransaction}>
                <div className='inputBoxCreateTransaction'>
                  <label>Titulo da Transação:</label>
                  <input
                    type='text'
                    name="title"
                    value={newTransaction.title}
                    onChange={handlenewTransactionChange}
                    required
                  />
                </div>
                <div className='inputBoxCreateTransaction'>
                  <label>Valor da Transação:</label>
                  <input
                    type='number'
                    name="amount"
                    value={newTransaction.amount}
                    onChange={handlenewTransactionChange}
                    required
                  />
                </div>
                <div className='inputBoxCreateTransaction'>
                  <label>Recebedor:</label>
                  <input
                    type='email'
                    name="receiver"
                    value={newTransaction.receiver}
                    onChange={handlenewTransactionChange}
                    required
                  />
                </div>
                <button type="submit" disabled={isLoading} className='buttonTransactions'>
                  {isLoading ? 'Efetuando...' : 'Efetuar'}
                </button>
              </form>
            </div>
          </div>
        )}

        {showEditModal && currentTransaction && (
          <div className="modal">
            <span className="closeButton" onClick={() => setShowEditModal(false)}>&times;</span>
            <div className="modal-content">
              <h3>Editar Transação</h3>
              <form className='transactionEditForm' onSubmit={handleEditTransaction}>
                <div className='transactionIdTitle'>
                  <div className='inputBoxTransaction'>
                    <label>ID:</label>
                    <p>{currentTransaction.transactionId}</p>
                  </div>
                  <div className='inputBoxTransaction'>
                    <label>Titulo da Transação:</label>
                    <input
                      type='text'
                      name="title"
                      value={currentTransaction.title}
                      onChange={handleEditChange}
                      required
                    />
                  </div>
                </div>
                <div className='transactionValorSender'>
                  <div className='inputBoxTransaction'>
                    <label>Valor:</label>
                    <p>{currentTransaction.amount}</p>
                  </div>
                  <div className='inputBoxTransaction'>
                    <label>Sender:</label>
                    <p>{currentTransaction.senderName}</p>
                  </div>
                </div>
                <div className='transactionEmailReceiver'>
                  <div className='inputBoxTransaction'>
                    <label>Receiver:</label>
                    <p>{currentTransaction.receiver}</p>
                  </div>
                  <div className='inputBoxTransaction'>
                    <label>Sender Email:</label>
                    <p>{currentTransaction.senderEmail}</p>
                  </div>
                </div>
                <div className='transactionDate'>
                  <div className='inputBoxTransaction'>
                    <label>Data:</label>
                    <p>{formatDateTime(currentTransaction.createdAt)}</p>
                  </div>
                </div>
                <button type="submit" disabled={isLoading} className='buttonTransactions'>
                  {isLoading ? 'Salvando...' : 'Salvar'}
                </button>
              </form>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default Transactions;
