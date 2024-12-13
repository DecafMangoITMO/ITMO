import { useState, useEffect } from "react";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../security/auth-context";
import axios from 'axios';
import axiosInstance from '../api/axios-instance';
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";

const lessMinutesSchema = z.object({
    minutes: z.preprocess((num) => parseFloat(z.string().parse(num), 10), z.number({ message: 'Минуты ожидания должны быть числом' }).gte(0, { message: 'Минуты ожидания должны быть положительными' })),
})

const deleteBySpeedSchema = z.object({
    speed: z.preprocess((num) => parseFloat(z.string().parse(num), 10), z.number({ message: 'Скорость влияния должна быть числом' }).gte(0, { message: 'Скорость влияния должна быть положительной' })),
})

const Menu = ({ setChoosenHumanBeings, setCurrentChoosenHumanBeingPage, setTotalChoosenHumanBeings }) => {
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const [user, setUser] = useState({
        username: '',
        role: '',
    });

    const token = localStorage.getItem('custom-auth-token');
    const { logout } = useAuth();
    const navigate = useNavigate();

    const {
        register: registerLessMinutes,
        handleSubmit: handleLessMinutes,
        formState: { errors: lessMinutesErrors }
    } = useForm({
        resolver: zodResolver(lessMinutesSchema)
    });

    const {
        register: registerDeleteBySpeed,
        handleSubmit: handleDeleteBySpeed,
        formState: { errors: deleteBySpeedErrors }
    } = useForm({
        resolver: zodResolver(deleteBySpeedSchema)
    });

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axiosInstance.get(
                    '/user/whoami',
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setUser(response.data);
            } catch (error) {
                if (axios.isAxiosError(error)) {
                    const status = error.response?.status;
                    if (status === 401) {
                        toast.error('Пользователь не авторизован');
                        navigate('/auth');
                    } else {
                        toast.error('Нет ответа от сервера');
                    }
                } else {
                    toast.error('Произошла ошибка');
                }
            }
        };

        fetchData();
    }, []);

    const onAdmin = async () => {
        try {
            await axiosInstance.post(
                '/admin',
                {},
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    },
                },
            );
            toast.success('Заявка успешно отправлена');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response?.status;
                if (status === 401) {
                    toast.error('Пользователь не авторизован');
                    navigate('/auth');
                } else if (status === 400) {
                    toast.success('Вы уже отправили заявку')
                } else {
                    toast.error('Нет ответа от сервера');
                }
            } else {
                toast.error('Произошла ошибка');
            }
        }
    }

    const onRandomByMaxMinutes = async () => {
        try {
            const response = await axiosInstance.get(
                '/human-being/random/max-minutes-of-waiting',
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    },
                },
            );
            setChoosenHumanBeings([response.data])
            setCurrentChoosenHumanBeingPage(1);
            setTotalChoosenHumanBeings(1);
            toast.success('Рандомный человек с максимальным временем ожидания получен');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response?.status;
                if (status === 401) {
                    toast.error('Пользователь не авторизован');
                    navigate('/auth');
                } else if (status === 400) {
                    toast.success('Вы уже отправили заявку')
                } else if (status === 404) {
                    toast.error('Не было найдено ни одного пользователя');
                } else {
                    toast.error('Нет ответа от сервера');
                }
            } else {
                toast.error('Произошла ошибка');
            }
        }
    }

    const onLessByMinutes = async (data) => {
        try {
            const response = await axiosInstance.get(
                '/human-being/less-minutes-of-waiting/' + data.minutes,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    },
                },
            );
            setChoosenHumanBeings(response.data)
            setCurrentChoosenHumanBeingPage(1);
            setTotalChoosenHumanBeings(response.data.length);
            toast.success('Люди получены');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response?.status;
                if (status === 401) {
                    toast.error('Пользователь не авторизован');
                    navigate('/auth');
                } else {
                    toast.error('Нет ответа от сервера');
                }
            } else {
                toast.error('Произошла ошибка');
            }
        }
    }

    const onGiveLada = async () => {
        try {
            const response = await axiosInstance.patch(
                '/human-being/lada-kalina',
                {},
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    },
                },
            );
            toast.success('Лады выданы');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response?.status;
                if (status === 401) {
                    toast.error('Пользователь не авторизован');
                    navigate('/auth');
                } else {
                    toast.error('Нет ответа от сервера');
                }
            } else {
                toast.error('Произошла ошибка');
            }
        }
    }

    const onDeleteByImpactSpeed = async (data) => {
        try {
            const response = await axiosInstance.delete(
                '/human-being/impact-speed/' + data.speed,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    },
                },
            );
            toast.success('Люди удалены');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response?.status;
                if (status === 401) {
                    toast.error('Пользователь не авторизован');
                    navigate('/auth');
                } else {
                    toast.error('Нет ответа от сервера');
                }
            } else {
                toast.error('Произошла ошибка');
            }
        }
    }

    const onDeleteWithToothpick = async () => {
        try {
            const response = await axiosInstance.delete(
                '/human-being/has-toothpick',
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    },
                },
            );
            toast.success('Люди удалены');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response?.status;
                if (status === 401) {
                    toast.error('Пользователь не авторизован');
                    navigate('/auth');
                } else {
                    toast.error('Нет ответа от сервера');
                }
            } else {
                toast.error('Произошла ошибка');
            }
        }
    }

    const toggleMenu = () => {
        setIsMenuOpen((prev) => !prev);
    };

    const exit = () => {
        logout();
        navigate('/auth');
    };

    return (
        <>
            {/* Button to toggle the profile menu */}
            <button
                className="bg-orange-500 text-white py-2 px-4 rounded hover:bg-orange-600 transition"
                onClick={toggleMenu}
            >
                Профиль
            </button>

            {/* Off-canvas menu */}
            <div
                className={`fixed top-0 right-0 h-full w-80 bg-gray-800 text-white transform transition-transform ${isMenuOpen ? 'translate-x-0' : 'translate-x-full'
                    } shadow-lg`}
            >
                <div className="p-4 flex justify-between items-center border-b border-gray-700">
                    <h2 className="text-lg font-bold">Профиль</h2>
                    <button
                        className="text-gray-400 hover:text-gray-200"
                        onClick={toggleMenu}
                    >
                        ✕
                    </button>
                </div>

                <div className='p-4 flex flex-col gap-4'>
                    <label>
                        Никнейм
                        <p className='w-full p-2 mt-1 rounded bg-gray-700 text-white'>{user.username}</p>
                    </label>
                    <label>
                        Роль
                        <p className='w-full p-2 mt-1 rounded bg-gray-700 text-white'>{user.role}</p>
                    </label>
                </div>

                {user.role === 'USER' && (
                    <div className='p-4 flex flex-col gap-4'>
                        <button
                            type="button"
                            className="bg-orange-500 text-white py-2 rounded hover:bg-orange-600 transition"
                            onClick={() => { onAdmin() }}
                        >
                            Стать администратором
                        </button>
                    </div>
                )}

                <div className='p-4 flex flex-col gap-4'>
                    <button
                        type="button"
                        className="bg-orange-500 text-white py-2 rounded hover:bg-orange-600 transition"
                        onClick={() => { onRandomByMaxMinutes() }}
                    >
                        Рандомный человек с максимальными минутами ожидания
                    </button>
                </div>

                <div className='p-4 flex flex-col gap-4'>
                    <button
                        type="button"
                        className="bg-orange-500 text-white py-2 rounded hover:bg-orange-600 transition"
                        onClick={handleLessMinutes(onLessByMinutes)}
                    >
                        С меньшими минутами ожидания
                    </button>
                    <input 
                        {...registerLessMinutes('minutes')}
                        className="p-3 rounded bg-gray-700 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-orange-500"
                    />
                    {lessMinutesErrors.minutes && <span className='text-red-500'>{lessMinutesErrors.minutes.message}</span>}
                </div>

                <div className='p-4 flex flex-col gap-4'>
                    <button
                        type="button"
                        className="bg-orange-500 text-white py-2 rounded hover:bg-orange-600 transition"
                        onClick={() => { onGiveLada() }}
                    >
                        Выдать Ладу
                    </button>
                </div>
                
                <div className='p-4 flex flex-col gap-4'>
                    <button
                        type="button"
                       
                        onClick={handleDeleteBySpeed(onDeleteByImpactSpeed)}
                        className="bg-orange-500 text-white py-2 rounded hover:bg-orange-600 transition"
                    >
                        Удалить со скоростью влияния
                    </button>
                    <input 
                        {...registerDeleteBySpeed('speed')}
                        className="p-3 rounded bg-gray-700 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-orange-500"
                    />
                    {deleteBySpeedErrors.speed && <span className='text-red-500'>{deleteBySpeedErrors.speed.message}</span>}
                </div>

                <div className='p-4 flex flex-col gap-4'>
                    <button
                        type="button"
                        className="bg-orange-500 text-white py-2 rounded hover:bg-orange-600 transition"
                        onClick={() => { onDeleteWithToothpick() }}
                    >
                        Удалить без зубочистки
                    </button>
                </div>

                <div className='p-4 flex flex-col gap-4'>
                    <button
                        type="button"
                        className="bg-orange-500 text-white py-2 rounded hover:bg-orange-600 transition"
                        onClick={exit}
                    >
                        Выход
                    </button>
                </div>
            </div>

        </>
    );
}

export default Menu;