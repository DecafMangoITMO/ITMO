import {useAuth} from "../provider/AuthProvider";
import {useNavigate} from "react-router-dom";
import {useState} from "react";
import axios from "axios";
import "../login.css";

const Login = () => {
    const {setUser} = useAuth();
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        username: "",
        password: "",
        isLogin: true
    });

    const [errors, setErrors] = useState({
        username: "",
        password: "",
    });

    const handleChange = (event) => {
        setFormData({...formData, [event.target.name]: event.target.value});
    };

    const handleRadioChange = (event) => {
        setFormData({
            ...formData,
            isLogin: event.target.value === "login"
        })
    };

    const validateForm = () => {
        let valid = true;
        let newErrors = {username: "", password: ""};

        if (formData.username.match(/^[A-Za-z0-9]{6,10}$/) === null) {
            newErrors.username = 'Username must be between 6 and 10 characters';
            valid = false;
        }

        if (formData.password.match(/^[A-Za-z0-9]{6,10}$/) === null) {
            newErrors.password = 'Password must be between 6 and 10 characters';
            valid = false;
        }

        setErrors(newErrors);
        return valid;
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        if (validateForm()) {

            const authType = formData.isLogin ? "login" : "register";
            axios.post(
                `http://localhost:8080/auth/${authType}`,
                JSON.stringify({
                    username: formData.username,
                    password: formData.password,
                }),
                {
                    headers: {
                        "Content-Type": "application/json",
                    }
                }
            ).then(response => {
                    const user = {
                        username: response.data.username,
                        role: response.data.role,
                        token: response.data.token,
                    }
                    setUser(user);
                    navigate("/");
                }
            ).catch(error => {
                if (error.response) {
                    if (error.response.status === 400) {
                        setErrors({username: "User already exists", password: ""});
                    }
                    if (error.response.status === 401) {
                        setErrors({username: "", password: "Invalid password"});
                    }
                    if (error.response.status === 404) {
                        setErrors({username: "User not found", password: ""});
                    }
                } else {
                    console.log(error)
                }
            })
        }
    };

    return (
        <div className="login-container">
            <form className="login-form" onSubmit={handleSubmit} >
                <h2>{formData.isLogin ? 'Login' : 'Register'}</h2>
                <div className="form-group">
                    <label >
                        Username:
                        <input
                            type="text"
                            name="username"
                            value={formData.username}
                            onChange={handleChange}
                        />
                        {errors.username && <span>{errors.username}</span>}
                    </label>
                </div>
                <div className="form-group">
                    <label>
                        Password:
                        <input
                            type="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                        />
                        {errors.password && <span>{errors.password}</span>}
                    </label>
                </div>
                <div className="radio-group">
                    <label>
                        <input
                            type="radio"
                            value="login"
                            checked={formData.isLogin}
                            onChange={handleRadioChange}
                        />
                        Login
                    </label>
                    <label>
                        <input
                            type="radio"
                            value="register"
                            checked={!formData.isLogin}
                            onChange={handleRadioChange}
                        />
                        Register
                    </label>
                </div>
                <button className="submit-button" type="submit">{formData.isLogin ? 'Login' : 'Register'}</button>
            </form>
        </div>
    );
}

export default Login;