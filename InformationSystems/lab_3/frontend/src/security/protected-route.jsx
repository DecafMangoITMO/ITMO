import { useAuth } from "./auth-context"
import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ element }) => {
    const { isAuthenticated } = useAuth();

    if (!isAuthenticated)
        return <Navigate to='/auth'/>;

    return element;
};

export default ProtectedRoute;