import React, { useState } from 'react';
import { useAuth } from '../security/auth-context';
import { useNavigate } from 'react-router-dom';
import 'react-toastify/dist/ReactToastify.css';
import { toast, ToastContainer } from 'react-toastify';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import axios from 'axios';
import axiosInstance from '../api/axios-instance';

const loginSchema = z.object({
    username: z.string().min(6, { message: 'Никнейм короче 6 символов' }).max(16, { message: 'Никнейм длиннее 16 символов' }),
    password: z.string().min(6, { message: 'Пароль короче 6 символов' }).max(16, { message: 'Пароль длиннее 16 символов' }),
});

const registerSchema = z.object({
    username: z.string().min(6, { message: 'Никнейм короче 6 символов' }).max(16, { message: 'Никнейм длиннее 16 символов' }),
    password: z.string().min(6, { message: 'Пароль короче 6 символов' }).max(16, { message: 'Пароль длиннее 16 символов' }),
    confirmPassword: z.string().min(6, { message: 'Пароль короче 6 символов' }).max(16, { message: 'Пароль длиннее 16 символов' }),
}).superRefine(({ password, confirmPassword }, ctx) => {
    if (password !== confirmPassword) {
        ctx.addIssue({
            code: 'custom',
            message: 'Пароли должны совпадать',
            path: ['confirmPassword'],
        })
    }
});

const Auth = () => {
    const [isLogin, setIsLogin] = useState(true);
    const { login } = useAuth();
    const navigate = useNavigate();

    const toggleMode = () => {
        setIsLogin(!isLogin);
    };

    const {
        register: loginRegister,
        handleSubmit: handleLogin,
        formState: { errors: loginErrors },
    } = useForm({
        resolver: zodResolver(loginSchema),
    });

    const {
        register: registerRegister,
        handleSubmit: handleRegister,
        formState: { errors: registerErrors },
    } = useForm({
        resolver: zodResolver(registerSchema),
    });

    const onLogin = async (data) => {
        try {
            const responce = await axiosInstance.post(
                '/auth/login',
                data,
            );
            login(responce.data.token);
            navigate('/home');
            toast.success('Вы успешно вошли');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response?.status;
                if (status === 401)
                    toast.error('Неверный пароль');
                else if (status === 404)
                    toast.error('Пользователь с таким никнеймом не найден');
                else
                    toast.error('Нет ответа от сервера');
            } else {
                toast.error('Произошла ошибка');
            }
        }
    };

    const onRegister = async (data) => {
        try {
            const responce = await axiosInstance.post(
                '/auth/register',
                data,
            );
            login(responce.data.token);
            navigate('/home');
            toast.success('Вы успешно зарегистрировались');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response?.status;
                if (status === 400)
                    toast.error('Пользователь с таким никнеймом уже существует');
                else
                    toast.error('Нет ответа от сервера');
            } else {
                toast.error('Произошла ошибка');
            }
        }
    }

    return (
        <div>
            <div className="flex items-center justify-center min-h-screen bg-gray-900">
                {isLogin && (
                    <div className="bg-gray-800 p-8 rounded-lg shadow-lg w-96">
                        <h2 className="text-2xl font-bold text-white text-center mb-6">
                            Вход
                        </h2>
                        <form onSubmit={handleLogin(onLogin)} className="flex flex-col gap-4">
                            <input
                                type="text"
                                placeholder='Никнейм'
                                {...loginRegister('username')}
                                className="p-3 rounded bg-gray-700 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-orange-500"
                            />
                            {loginErrors.username && <span className='text-red-500'>{loginErrors.username.message}</span>}

                            <input
                                type="password"
                                placeholder="Пароль"
                                {...loginRegister('password')}
                                className="p-3 rounded bg-gray-700 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-orange-500"
                            />
                            {loginErrors.password && <span className='text-red-500'>{loginErrors.password.message}</span>}

                            <button
                                type="submit"
                                className="bg-orange-500 text-white py-3 rounded font-bold hover:bg-orange-600 transition-colors"
                            >
                                Войти
                            </button>
                        </form>
                        <p className="text-gray-400 text-center mt-4">
                            Нет аккаунта?{' '}
                            <span
                                onClick={toggleMode}
                                className="text-orange-500 font-bold cursor-pointer hover:underline"
                            >
                                Регистрация
                            </span>
                        </p>
                    </div>
                )}
                {!isLogin && (
                    <div className="bg-gray-800 p-8 rounded-lg shadow-lg w-96">
                        <h2 className="text-2xl font-bold text-white text-center mb-6">
                            Вход
                        </h2>
                        <form onSubmit={handleRegister(onRegister)} className="flex flex-col gap-4">
                            <input
                                type="text"
                                placeholder='Никнейм'
                                {...registerRegister('username')}
                                className="p-3 rounded bg-gray-700 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-orange-500"
                            />
                            {registerErrors.username && <span className='text-red-500'>{registerErrors.username.message}</span>}

                            <input
                                type="password"
                                placeholder="Пароль"
                                {...registerRegister('password')}
                                className="p-3 rounded bg-gray-700 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-orange-500"
                            />
                            {registerErrors.password && <span className='text-red-500'>{registerErrors.password.message}</span>}

                            <input
                                type="password"
                                placeholder="Повторите пароль"
                                {...registerRegister('confirmPassword')}
                                className="p-3 rounded bg-gray-700 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-orange-500"
                            />
                            {registerErrors.confirmPassword && <span className='text-red-500'>{registerErrors.confirmPassword.message}</span>}

                            <button
                                type="submit"
                                className="bg-orange-500 text-white py-3 rounded font-bold hover:bg-orange-600 transition-colors"
                            >
                                Зарегистрироваться
                            </button>
                        </form>
                        <p className="text-gray-400 text-center mt-4">
                            Уже зарегистрированы?{' '}
                            <span
                                onClick={toggleMode}
                                className="text-orange-500 font-bold cursor-pointer hover:underline"
                            >
                                Войти
                            </span>
                        </p>
                    </div>
                )}
                <ToastContainer position="bottom-left" autoClose={3000} />
            </div>
        </div>
    );
}

export default Auth;