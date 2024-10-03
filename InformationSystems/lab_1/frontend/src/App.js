import logo from './logo.svg';
import './App.css';
import AuthProvider from "./provider/AuthProvider";
import Routes from "./routes/Routes";

function App() {
  return (
      <AuthProvider>
        <Routes/>
      </AuthProvider>
  );
}

export default App;
