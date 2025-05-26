
import { useState } from 'react';
import HomePage from './components/HomePage';
import Login from './components/Login';
import Register from './components/Register';
import Profile from './components/Profile';
import Cards from './components/Cards';
import Transactions from './components/Transactions';
import './css/global.css';


function App() {
  const [showLogin, setShowLogin] = useState(false);
  const [showRegister, setShowRegister] = useState(false);
  const [showProfile, setShowProfile] = useState(false);
  const [showCards, setShowCards] = useState(false);
  const [showTransactions, setShowTransactions] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);


  const handleLoginClick = () => {
    setShowLogin(true);
    setShowRegister(false);
    setShowProfile(false);
    setShowCards(false);
    setShowTransactions(false);
  };

  const handleRegisterClick = () => {
    setShowRegister(true);
    setShowLogin(false);
    setShowProfile(false);
    setShowCards(false);
    setShowTransactions(false);
  };

  const handleProfileClick = () => {
    if (!!isLoggedIn){
    setShowProfile(true);
    setShowLogin(false);
    setShowRegister(false);
    setShowCards(false);
    setShowTransactions(false);
    } else{
      setShowLogin(true);
    }
  };

   const handleCardsClick = () => {
    if(!!isLoggedIn){
      setShowCards(true);
    setShowProfile(false);
    setShowLogin(false);
    setShowRegister(false);
    setShowTransactions(false);
    } else{
      setShowLogin(true);
    }
  };

   const handleTransactionsClick = () => {
    if(!!isLoggedIn){
    setShowTransactions(true);
    setShowProfile(false);
    setShowLogin(false);
    setShowRegister(false);
    setShowCards(false);
    } else{
      setShowLogin(true);
    }
    
  };

  const closeForms = () => {
    setShowLogin(false);
    setShowRegister(false);
    setShowProfile(false);
    setShowCards(false);
    setShowTransactions(false);
  };

  return (
    <div className="App">
      <HomePage 
        onLoginClick={handleLoginClick} 
        onRegisterClick={handleRegisterClick}
        onProfileClick={handleProfileClick}
        onCardsClick={handleCardsClick}
        onTransactionsClick={handleTransactionsClick}
        onHomeClick={closeForms}
      />
      {showLogin && (
        <div className="modal-container">
          <Login onClose={closeForms} setIsLoggedIn={setIsLoggedIn}/>
        </div>
      )}
      {showRegister && (
        <div className="modal-container">
          <Register onClose={closeForms} />
        </div>
      )}
      {showProfile && (
        <div className="modal-container">
          <Profile onClose={closeForms} setIsLoggedIn ={setIsLoggedIn} />
        </div>
      )}
      {showCards && (
        <div className="modal-container">
          <Cards  onClose={closeForms} />
        </div>
      )}
      {showTransactions && (
        <div className="modal-container">
          <Transactions  onClose={closeForms} />
        </div>
      )}
    </div>
  );
}

export default App;