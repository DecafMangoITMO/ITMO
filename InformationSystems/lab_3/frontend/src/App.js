import logo from './logo.svg';
import './App.css';
import { AuthProvider } from './security/auth-context';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Auth from './pages/auth';
import ProtectedRoute from './security/protected-route';
import Home from './pages/home';
import NotFound from './pages/not-found';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path='/auth' element={ <Auth /> } />
          <Route path='/home' element={
            <ProtectedRoute element={ <Home /> }/>
          } />
        <Route path='*' element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
