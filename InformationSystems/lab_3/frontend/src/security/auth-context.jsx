const { createContext, useState, useContext } = require("react");

const AuthContext = createContext(undefined);

export const AuthProvider = ({ children }) => {
    const token = localStorage.getItem('custom-auth-token');

    const [isAuthenticated, setIsAuthenticated] = useState(token ? true : false);

    const login = (token) => {
        localStorage.setItem('custom-auth-token', token);
        setIsAuthenticated(true);
    };
    const logout = () => {
        localStorage.removeItem('custom-auth-token');
        setIsAuthenticated(false);
    }

    return (
        <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context)
        throw new Error('useAuth must be used within an AuthProvider');
    return context;
};