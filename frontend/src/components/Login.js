import '../css/global.css';
import '../css/forms.css';
import { useState } from 'react';

function LoginForm({ onClose, setIsLoggedIn}) {
  const [formData, setFormData] = useState({
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
        const response = await fetch('http://localhost:8080/api/auth/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(formData)
        });
  
        const data = await response.text();
  
        if (!response.ok) {
          throw new Error(data.message || 'Credenciais inválidas.');
        }

        localStorage.setItem('authToken', data);

        setSuccess(true);
        setIsLoggedIn(true);
        setTimeout(() => {
          onClose(); 
          setSuccess(false);
          setFormData({email: '', password: '' }); 
        }, 1000);
        
      } catch (err) {
        setError(err.message || 'Ocorreu um erro durante o login');
      } finally {
        setIsLoading(false);
      }
    };

  return (
    <div className='wrapper'>
      <span className="closeButton" onClick={onClose}>&times;</span>
      <div className='LoginForm'>
        <h2>Login</h2>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className='inputBoxLogin'>
            <ion-icon className="icon"name="mail-outline"></ion-icon>
            <input type='email' 
            name="email"
            value={formData.email}
            onChange={handleChange} 
            required />
            <label>Email</label>
          </div>
          <div className='inputBoxLogin'>
            <ion-icon  className="icon"name="lock-closed-outline"></ion-icon>
            <input  
            name="password"
            value={formData.password}
            onChange={handleChange}type='password' 
            required />
            <label>Password</label>
          </div>
          <button type="submit" 
          className="submitButtonLogin"
           disabled={isLoading}>
            {isLoading ? 'Logando...' : 'Login'}
            </button>
        </form>
      </div>
    </div>
  );
}

export default LoginForm;   