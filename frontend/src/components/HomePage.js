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
        <Link onClick={onCardsClick} to="/cards">Cards</Link>
        <Link onClick={onTransactionsClick} to="/transactions">Transactions</Link>
        <Link onClick={onProfileClick} to="/profile">Profile</Link>
      </nav>

      <div className="buttons">
        {isLoggedIn ? (
          <>
            <p>Olá, {userName} </p>
            <button onClick={onLogoutClick} className="logoutButton">Sair</button>
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