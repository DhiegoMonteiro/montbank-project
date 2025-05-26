import '../css/forms.css';
import { useState, useEffect } from 'react';

function Profile({ onClose, setIsLoggedIn }) {
  const [formData, setFormData] = useState({
    name: '',
    CPF: '',
    email: ''
  });
  
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);
  useEffect(() => {
    const fetchProfile = async () => {
      setIsLoading(true);
      setError(null);
      try {
        const response = await fetch('http://localhost:8080/api/auth/user/profile', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
          }
        });

        if (!response.ok) {
          throw new Error('Não foi possível carregar o perfil, verifique se está logado.');
        }

        const data = await response.json();
        setFormData({
          name: data.name ,
          CPF: data.CPF ,
          email: data.email
        });
      } catch (err) {
        setError(err.message);
      } finally {
        setIsLoading(false);
      }
    };

    fetchProfile();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);

     const request = JSON.stringify(formData);
    try {
      const response = await fetch('http://localhost:8080/api/auth/user/profile/edit', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`
        },
        body: request
      });

      if (!response.ok) {
        const data = await response.json();
        throw new Error(data.message || 'Erro ao atualizar perfil');
      }
      setSuccess(true);
      setTimeout(() => {
        onClose();
        setSuccess(false);
      }, 1000);
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleDelete = async (e) => {
    if (!window.confirm('Tem certeza que deseja deletar seu perfil? Esta ação não poderá ser desfeita.')) return;

    e.preventDefault();
    setIsLoading(true);
    setError(null);

    try {
      const response = await fetch('http://localhost:8080/api/auth/user/profile/delete', {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`
        }
      });

      if (!response.ok) {
        const data = await response.json();
        throw new Error(data.message || 'Erro ao deletar perfil');
      }
      localStorage.clear();
      setIsLoggedIn(false);
      setTimeout(() => {
        onClose();
      }, 1000);
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className='wrapper'>
      <span className="closeButton" onClick={onClose}>&times;</span>
      <div className='ProfileForm'>
        <h2>Perfil</h2>

        {isLoading && <div className="loading-message">Carregando...</div>}
        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">Perfil atualizado com sucesso!</div>}

        <form onSubmit={handleSubmit}>
          <div className='inputBoxProfile'>
            <ion-icon className="icon" name="person-circle-outline"></ion-icon>
            <input 
              type='text' 
              name="name"
              value={formData.name}
              onChange={handleChange}
              required 
            />
            <label>Name</label>
          </div>
          <div className='inputBoxProfile'>
            <ion-icon className="icon" name="id-card-outline"></ion-icon>
            <input 
              type='text' 
              name="CPF"
              value={formData.CPF}
              onChange={handleChange}
              required 
            />
            <label>CPF</label>
          </div>
          <div className='inputBoxProfile'>
            <ion-icon className="icon" name="mail-outline"></ion-icon>
            <input 
              type='email' 
              name="email"
              value={formData.email}
              onChange={handleChange}
              required 
            />
            <label>Email</label>
          </div>
          <button 
            type="submit" 
            className="submitButtonProfile"
            disabled={isLoading}
          >
            {isLoading ? 'Salvando...' : 'Salvar'}
          </button>
        </form>
        <button 
        onClick = {handleDelete}
          className="buttonDeleteUser"
          disabled={isLoading}
          >
          {isLoading ? 'Deletando...' : 'Deletar'}
        </button>
      </div>
    </div>
  );
}

export default Profile;
