import '../css/forms.css';
import { useState, useEffect } from 'react';

function Cards({ onClose }) {
  const [cards, setCards] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [currentCard, setCurrentCard] = useState(null);

  const [newCard, setNewCard] = useState({
    cardName: '',
    type: ''
  });

  const fetchCards = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await fetch('http://localhost:8080/api/cards/my-cards', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
        }
      });
      if (!response.ok) {
        throw new Error('Não foi possível carregar seus cartões, verifique se está logado.');
      }
      const data = await response.json();
      setCards(data);
    } catch (err) {
      setError("Não foi possivel listar seus cartões, por favor, tente novamente mais tarde.");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchCards();
  }, []);

  const handleNewCardChange = (e) => {
    const { name, value } = e.target;
    setNewCard(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleCreateCard = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);
    try {
      const response = await fetch('http://localhost:8080/api/cards/my-cards/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`
        },
        body: JSON.stringify(newCard)
      });
      if (!response.ok) {
        const data = await response.json();
        throw new Error(data.message || 'Erro ao criar cartão');
      }
      setShowCreateModal(false);
      setNewCard({ cardName: '', type: '' });
      fetchCards();
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  const openEditModal = (card) => {
    setCurrentCard(card);
    setShowEditModal(true);
    setShowCreateModal(false);
  };

  const handleEditChange = (e) => {
    const { name, value } = e.target;
    setCurrentCard(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleEditCard = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);
    try {
      const response = await fetch(`http://localhost:8080/api/cards/my-cards/${currentCard.cardId}/edit`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`
        },
        body: JSON.stringify({
          cardName: currentCard.cardName,
          type: currentCard.type
        })
      });
      if (!response.ok) {
        const data = await response.json();
        throw new Error(data.message || 'Erro ao editar cartão');
      }
      setShowEditModal(false);
      setCurrentCard(null);
      fetchCards();
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeleteCard = async (cardId) => {
    if (!window.confirm('Tem certeza que deseja deletar este cartão?')) return;

    setIsLoading(true);
    setError(null);
    try {
      const response = await fetch(`http://localhost:8080/api/cards/my-cards/${cardId}/delete`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`
        }
      });
      if (!response.ok) {
        const data = await response.json();
        throw new Error(data.message || 'Erro ao deletar cartão');
      }
      fetchCards();
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className='wrapperForm'>
      <span className="closeButton" onClick={onClose}>&times;</span>
      <div className='CardForm'>
        <h2>Meus Cartões</h2>

        {isLoading && <div className="loading-message">Carregando...</div>}
        {error && <div className="error-message">{error}</div>}
        <div className='generateCardButton'>
          <button onClick={() => { setShowCreateModal(true); setShowEditModal(false); }}>Gerar novo cartão</button>
        </div>
        {cards.length === 0 ? (
          <div>
            <p>Nenhum cartão cadastrado.</p>
          </div>
        ) : (
          <ul>
            {cards.map((card) => (
              <li key={card.cardId}>
                <strong>{card.cardName} - {card.type} </strong> 
                <button className='listButton' onClick={() => openEditModal(card)}>Visualizar</button>
                <button className='listButton' onClick={() => handleDeleteCard(card.cardId)}>Deletar</button>
              </li>
            ))}
          </ul>
        )}

        {showCreateModal && (
          <div className="modal">
            <span className="closeButton" onClick={() => setShowCreateModal(false)}>&times;</span>
            <div className="modal-content">
              <h3>Gerar Novo Cartão</h3>
              <form onSubmit={handleCreateCard}>
                <div className='inputBoxCreateCards'>
                  <label>Nome do Cartão</label>
                  <input
                    type='text'
                    name="cardName"
                    value={newCard.cardName}
                    onChange={handleNewCardChange}
                    required
                  />
                  <label>Tipo do Cartão:</label>
                  <label>
                    <input
                      type="radio"
                      name="type"
                      value="Crédito"
                      checked={newCard.type === "Crédito"}
                      onChange={handleNewCardChange}
                      required
                    />
                    Crédito
                  </label>
                  <label>
                    <input
                      type="radio"
                      name="type"
                      value="Débito"
                      checked={newCard.type === "Débito"}
                      onChange={handleNewCardChange}
                    />
                    Débito
                  </label>
                </div>
                <button type="submit" disabled={isLoading} className='buttonCards'>
                  {isLoading ? 'Gerando...' : 'Gerar'}
                </button>
              </form>
            </div>
          </div>
        )}

        {showEditModal && currentCard && (
          <div className="modal">
            <span className="closeButton" onClick={() => setShowEditModal(false)}>&times;</span>
            <div className="modal-content">
              <h3>Editar Cartão</h3>
              <form className='cardEditForm' onSubmit={handleEditCard}>
                <div className='cardNameNumber'>
                  <div className='inputBoxCards'>
                    <label>Nome do Cartão:  </label>
                    <input
                      type='text'
                      name="cardName"
                      value={currentCard.cardName}
                      onChange={handleEditChange}
                      required
                    />
                  </div>

                  <div className='inputBoxCards'>
                    <label>Número do Cartão:</label>
                    <p>{currentCard.number}</p>
                  </div>
                </div>
                <div className='cardCvvDate'>
                  <div className='inputBoxCards'>
                    <label>CVV:</label>
                    <p>{currentCard.cvv}</p>
                  </div>

                  <div className='inputBoxCards'>
                    <label>Data de Expiração:</label>
                    <p>{currentCard.expireDate}</p>
                  </div>
                </div>
                <div className='cardOwnerType'>
                  <div className='inputBoxCards'>
                    <label>Titular:</label>
                    <p>{currentCard.ownerName}</p>
                  </div>

                  <div className='inputBoxCards'>
                    <label>Tipo do Cartão:</label>
                    <label>
                      <input
                        type="radio"
                        name="type"
                        value="Crédito"
                        checked={currentCard.type === "Crédito"}
                        onChange={handleEditChange}
                        required
                      />
                      Crédito
                    </label>
                    <label>
                      <input
                        type="radio"
                        name="type"
                        value="Débito"
                        checked={currentCard.type === "Débito"}
                        onChange={handleEditChange}
                      />
                      Débito
                    </label>
                  </div>
                </div>

                <button type="submit" disabled={isLoading} className='buttonCards'>
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

export default Cards;
