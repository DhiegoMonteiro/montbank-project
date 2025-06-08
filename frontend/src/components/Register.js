import '../css/forms.css';
import { useState } from 'react';

function RegisterForm({ onClose }) {
  const [formData, setFormData] = useState({
    name: '',
    CPF: '',
    email: '',
    password: ''
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

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

    try {
      const response = await fetch('http://localhost:8081/api/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || 'Não foi possível concluir o registro');
      }

      setSuccess(true);
      setTimeout(() => {
        onClose(); 
        setSuccess(false);
        setFormData({ name: '', CPF: '', email: '', password: '' }); 
      }, 1000);
      
    } catch (err) {
      setError(err.message || 'Ocorreu um erro durante o registro');
      setTimeout(() => setError(null), 2000);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className='wrapper'>
      <span className="closeButton" onClick={onClose}>&times;</span>
      <div className='RegisterForm'>
        <h2>Register</h2>
        
        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">Registro concluído com sucesso!</div>}
        
        <form onSubmit={handleSubmit}>
          <div className='inputBoxRegister'>
            <ion-icon className="icon" name="person-circle-outline"></ion-icon>
            <input 
              type='text' 
              name="name"
              value={formData.name}
              onChange={handleChange}
              required 
            />
            <label>Nome</label>
          </div>
          <div className='inputBoxRegister'>
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
          <div className='inputBoxRegister'>
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
          <div className='inputBoxRegister'>
            <ion-icon className="icon" name="lock-closed-outline"></ion-icon>
            <input 
              type='password' 
              name="password"
              value={formData.password}
              onChange={handleChange}
              required 
            />
            <label>Password</label>
          </div>
          <button 
            type="submit" 
            className="submitButtonRegister"
            disabled={isLoading}
          >
            {isLoading ? 'Registrando...' : 'Register'}
          </button>
        </form>
      </div>
    </div>
  );
}

export default RegisterForm;