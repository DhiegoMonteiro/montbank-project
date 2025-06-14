import '../css/global.css';
import { Link } from 'react-router-dom';
import MontbankLogo from '../images/MontbankLogo.png';

function HomePage({ onRegisterClick, onLoginClick, onProfileClick, onCardsClick, onTransactionsClick, onHomeClick, onLogoutClick, isLoggedIn, userName }) {
  return (
    <header>
      <div className='logo-container'>
        <img src={MontbankLogo} className='logo-img' alt='Logo' />
        <h2 className="logo">MontBank</h2>
      </div>
      <nav className="navigation">
        <Link onClick={onHomeClick} to="/">Home</Link>
        <Link onClick={onCardsClick} to="/cards">Cartões</Link>
        <Link onClick={onTransactionsClick} to="/transactions">Transações</Link>
        <Link onClick={onProfileClick} to="/profile">Perfil</Link>
      </nav>

      <div className="buttons">
        {isLoggedIn ? (
          <>
            <div className='logged'>
              <p>Olá, <strong>{userName} </strong></p>
              <button onClick={onLogoutClick} className="logoutButton">Sair</button>
            </div>
          </>
        ) : (
          <>
            <button onClick={onLoginClick} className="loginButton">Login</button>
            <button onClick={onRegisterClick} className="registerButton">Register</button>
          </>
        )}
      </div>
    </header>
  );
}


export default HomePage;