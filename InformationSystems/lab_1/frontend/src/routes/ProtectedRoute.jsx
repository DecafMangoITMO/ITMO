import {useAuth} from "../provider/AuthProvider";
import {Navigate, Outlet} from "react-router-dom";

export const ProtectedRoute = () => {
    const {user} = useAuth();

    if (!user) {
        return <Navigate to="/login"/>;
    }

    return <Outlet/>;
};
