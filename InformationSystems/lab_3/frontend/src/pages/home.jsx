import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Menu from '../components/menu';
import axios from 'axios';
import axiosInstance from '../api/axios-instance';
import { toast } from 'react-toastify';
import { CreateCarModal, UpdateCarModal } from '../components/car-modal';
import { ToastContainer } from 'react-toastify';
import { CreateCoordinatesModal, UpdateCoordinatesModal } from '../components/coordinates-modal';
import { CreateHumanBeingModal, UpdateHumanBeingModal } from '../components/humanbeing-modal';

import { Map, Marker, Popup } from 'pigeon-maps';
import ImportModal from '../components/import-modal';

const Home = () => {
    const [user, setUser] = useState(null);

    const [humanBeings, setHumanBeings] = useState([]);
    const [currentHumanBeingPage, setCurrentHumanBeingPage] = useState(1);
    const [humanBeingsPerPage] = useState(5);
    const [totalHumanBeings, setTotalHumanBeings] = useState(0);

    const [choosenHumanBeings, setChoosenHumanBeings] = useState([]);
    const [currentChoosenHumanBeingPage, setCurrentChoosenHumanBeingPage] = useState(1);
    const [choosenHumanBeingsPerPage] = useState(5);
    const [totalChoosenHumanBeings, setTotalChoosenHumanBeings] = useState(0);

    const [coordinates, setCoordinates] = useState([]);
    const [currentCoordinatesPage, setCurrentCoordinatesPage] = useState(1);
    const [coordinatesPerPage] = useState(5);
    const [totalCoordinates, setTotalCoordinates] = useState(0);

    const [cars, setCars] = useState([]);
    const [currentCarPage, setCurrentCarPage] = useState(1);
    const [carsPerPage] = useState(5);
    const [totalCars, setTotalCars] = useState(0);

    const [requests, setRequests] = useState([]);
    const [currentRequestPage, setCurrentRequestPage] = useState(1);
    const [requestsPerPage] = useState(5);
    const [totalRequests, setTotalRequests] = useState(0);

    const [importAttempts, setImportAttempts] = useState([]);
    const [currentImportAttemptPage, setCurrentImportAttemptPage] = useState(1);
    const [importAttemptsPerPage] = useState(5);
    const [totalImportAttempts, setTotalImportAttempts] = useState(0);

    const token = localStorage.getItem('custom-auth-token');
    const navigate = useNavigate();

    const fetchData = async () => {
        let currUser = null;
        try {
            const response = await axiosInstance.get(
                '/user/whoami',
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            currUser = response.data;
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
        };

        try {
            const response = await axiosInstance.get(
                '/car/count',
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            setTotalCars(response.data);
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
        };

        try {
            const response = await axiosInstance.get(
                '/car?from=' + ((currentCarPage - 1) * carsPerPage) + '&size=' + carsPerPage,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            setCars(response.data);
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
        };

        try {
            const response = await axiosInstance.get(
                '/coordinates/count',
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            setTotalCoordinates(response.data);
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
        };

        try {
            const response = await axiosInstance.get(
                '/coordinates?from=' + (currentCoordinatesPage - 1) + '&size=' + coordinatesPerPage,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            setCoordinates(response.data);
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
        };

        try {
            const response = await axiosInstance.get(
                '/human-being/count',
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            setTotalHumanBeings(response.data);
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
        };

        try {
            const response = await axiosInstance.get(
                '/human-being?from=' + ((currentHumanBeingPage - 1) * humanBeingsPerPage) + '&size=' + humanBeingsPerPage,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            setHumanBeings(response.data);
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
        };

        if (currUser !== null && currUser.role === 'ADMIN') {
            try {
                const response = await axiosInstance.get(
                    '/admin/count',
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setTotalRequests(response.data);
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
            };
        }

        if (currUser !== null && currUser.role === 'ADMIN') {
            try {
                const response = await axiosInstance.get(
                    '/admin?from=' + ((currentHumanBeingPage - 1) * humanBeingsPerPage) + '&size=' + humanBeingsPerPage,
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setRequests(response.data);
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
            };
        }

        try {
            const response = await axiosInstance.get(
                '/import/count',
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            setTotalImportAttempts(response.data);
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
        };

        try {
            const response = await axiosInstance.get(
                '/import?from=' + ((currentImportAttemptPage - 1) * importAttemptsPerPage) + '&size=' + importAttemptsPerPage,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            setImportAttempts(response.data);
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
        };

        setCurrentHumanBeingPage(1);
        setCurrentCarPage(1);
        setCurrentCoordinatesPage(1);
        setCurrentRequestPage(1);
        setCurrentImportAttemptPage(1);
    };

    useEffect(() => {
        const ws = new WebSocket("ws://localhost:8080/socket");

        ws.onmessage = (ev) => {
            fetchData();
        };

        fetchData();

        return () => {
            ws.close();
        }
    }, []);

    useEffect(() => {
        const fetchPageData = async () => {
            try {
                const response = await axiosInstance.get(
                    '/human-being/count',
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setTotalHumanBeings(response.data);
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
            };

            try {
                const response = await axiosInstance.get(
                    '/human-being?from=' + ((currentHumanBeingPage - 1) * humanBeingsPerPage) + '&size=' + humanBeingsPerPage,
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setHumanBeings(response.data);
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
            };
        };

        fetchPageData();
    }, [currentHumanBeingPage]);

    useEffect(() => {
        const fetchPageData = async () => {
            try {
                const response = await axiosInstance.get(
                    '/car/count',
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setTotalCars(response.data);
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
            };

            try {
                const response = await axiosInstance.get(
                    '/car?from=' + ((currentCarPage - 1) * carsPerPage) + '&size=' + carsPerPage,
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setCars(response.data);
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
            };
        };

        fetchPageData();
    }, [currentCarPage]);

    useEffect(() => {
        const fetchPageData = async () => {
            try {
                const response = await axiosInstance.get(
                    '/coordinates/count',
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setTotalCoordinates(response.data);
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
            };

            try {
                const response = await axiosInstance.get(
                    '/coordinates?from=' + ((currentCoordinatesPage - 1) * coordinatesPerPage) + '&size=' + coordinatesPerPage,
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setCoordinates(response.data);
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
            };
        };

        fetchPageData();
    }, [currentCoordinatesPage]);

    useEffect(() => {
        const fetchPageData = async () => {
            if (user !== null && user !== null && user.role === 'ADMIN') {
                try {
                    const response = await axiosInstance.get(
                        '/admin/count',
                        {
                            headers: {
                                'Authorization': 'Bearer ' + token,
                            }
                        },
                    );
                    setTotalRequests(response.data);
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
                };
            }

            if (user !== null && user.role === 'ADMIN') {
                try {
                    const response = await axiosInstance.get(
                        '/admin?from=' + ((currentHumanBeingPage - 1) * humanBeingsPerPage) + '&size=' + humanBeingsPerPage,
                        {
                            headers: {
                                'Authorization': 'Bearer ' + token,
                            }
                        },
                    );
                    setRequests(response.data);
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
                };
            }
        };

        fetchPageData();
    }, [currentRequestPage]);

    useEffect(() => {
        const fetchPageData = async () => {
            try {
                const response = await axiosInstance.get(
                    '/import/count',
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setTotalImportAttempts(response.data);
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
            };

            try {
                const response = await axiosInstance.get(
                    '/import?from=' + ((currentImportAttemptPage - 1) * importAttemptsPerPage) + '&size=' + importAttemptsPerPage,
                    {
                        headers: {
                            'Authorization': 'Bearer ' + token,
                        }
                    },
                );
                setImportAttempts(response.data);
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
            };
        };

        fetchPageData();
    }, [currentImportAttemptPage]);

    const prevChoosenHumanBeingPage = async () => {
        setCurrentChoosenHumanBeingPage(currentChoosenHumanBeingPage => currentChoosenHumanBeingPage - 1);
    }

    const nextChoosenHumanBeingPage = async () => {
        setCurrentChoosenHumanBeingPage(currentChoosenHumanBeingPage => currentChoosenHumanBeingPage + 1);
    }

    const prevHumanBeingPage = async () => {
        setCurrentHumanBeingPage(currentHumanBeingPage => currentHumanBeingPage - 1);
    }

    const nextHumanBeingPage = async () => {
        setCurrentHumanBeingPage(currentHumanBeingPage => currentHumanBeingPage + 1);
    }

    const prevCarPage = async () => {
        setCurrentCarPage(currentCarPage => currentCarPage - 1);
    }

    const nextCarPage = async () => {
        setCurrentCarPage(currentCarPage => currentCarPage + 1);
    }

    const prevCoordinatesPage = async () => {
        setCurrentCoordinatesPage(currentCoordinatesPage => currentCoordinatesPage - 1);
    }

    const nextCoordinatesPage = async () => {
        setCurrentCoordinatesPage(currentCoordinatesPage => currentCoordinatesPage + 1);
    }

    const prevRequestPage = async () => {
        setCurrentRequestPage(currentRequestPage => currentRequestPage - 1);
    }

    const nextRequestPage = async () => {
        setCurrentRequestPage(currentRequestPage => currentRequestPage + 1);
    }

    const prevImportAttemptPage = async () => {
        setCurrentImportAttemptPage(currentImportAttemptPage => currentImportAttemptPage - 1);
    }

    const nextImportAttemptPage = async () => {
        setCurrentImportAttemptPage(currentImportAttemptPage => currentImportAttemptPage + 1);
    }

    const onHumanBeingDelete = async (id) => {
        try {
            const response = await axiosInstance.delete(
                '/human-being/' + id,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            toast.success('Человек успешно удален');
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
        };
    }

    const onCarDelete = async (id) => {
        try {
            const response = await axiosInstance.delete(
                '/car/' + id,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            toast.success('Автомобиль успешно удален');
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
        };
    }

    const onCoordinatesDelete = async (id) => {
        try {
            const response = await axiosInstance.delete(
                '/coordinates/' + id,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            toast.success('Координаты успешно удалены');
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
        };
    };

    const onRequestApprove = async (id) => {
        try {
            const response = await axiosInstance.put(
                '/admin/' + id,
                {},
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            toast.success('Запрос успешно одобрен');
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
        };
    };

    const onFileDownload = async (filename) => {
        try {
            const response = await axiosInstance.get(
                '/import/file/' + filename,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    },
                    responseType: 'blob'
                },
            );

            const urlBlob = window.URL.createObjectURL(new Blob([response.data]));

            const link = document.createElement('a');
            link.href = urlBlob;
            link.setAttribute('download', filename);

            document.body.appendChild(link);
            link.click();

            link.remove();
        
            window.URL.revokeObjectURL(urlBlob);
        
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response?.status;
                if (status === 401) {
                    toast.error('Пользователь не авторизован');
                    navigate('/auth');
                } else if (status === 404) {
                    toast.error('Требуемого файла не существует');
                } else {
                    toast.error('Нет ответа от сервера');
                }
            } else {
                toast.error('Произошла ошибка');
            }
        };
    }

    return (
        <div className="min-h-screen bg-gray-900 text-white p-8">
            <header className="flex justify-between items-center mb-8">
                <h1 className="text-3xl font-bold">Главная страница</h1>
                <Menu setChoosenHumanBeings={setChoosenHumanBeings} setCurrentChoosenHumanBeingPage={setCurrentChoosenHumanBeingPage} setTotalChoosenHumanBeings={setTotalChoosenHumanBeings} />
            </header>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                <div className="bg-gray-800 p-6 rounded-lg shadow-lg">
                    <Map height={300} defaultCenter={[10, 0]} defaultZoom={7}>
                        {
                            humanBeings.map((humanBeing) => (
                                <Marker
                                    key={humanBeing.id}
                                    anchor={[humanBeing.coordinates.x, humanBeing.coordinates.y]}
                                    onClick={() => {
                                        setChoosenHumanBeings([humanBeing]);
                                        setTotalChoosenHumanBeings(1);
                                        setCurrentChoosenHumanBeingPage(1)
                                    }}
                                />
                            ))
                        }
                    </Map>
                </div>

                <div className="bg-gray-800 p-6 rounded-lg shadow-lg">
                    <div className='flex justify-between items-center'>
                        <h2 className="text-2xl font-bold mb-4">Выбранные люди</h2>
                    </div>
                    <table className="w-full mb-3">
                        <thead>
                            <tr className="border-b border-gray-700">
                                <th className="text-left py-2">ID</th>
                                <th className="text-left py-2">Имя</th>
                                <th className="text-left py-2">Минуты ожидания</th>
                            </tr>
                        </thead>
                        <tbody>
                            {choosenHumanBeings.slice((currentChoosenHumanBeingPage - 1) * humanBeingsPerPage, currentChoosenHumanBeingPage * humanBeingsPerPage - 1).map((choosenHumanBeing) => (
                                <tr key={choosenHumanBeing.id} className="border-b border-gray-700">
                                    <td className="py-2">{choosenHumanBeing.id}</td>
                                    <td className="py-2">{choosenHumanBeing.name}</td>
                                    <td className="py-2">{choosenHumanBeing.minutesOfWaiting}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    <div className="flex justify-between items-center">
                        <button
                            onClick={() => { prevChoosenHumanBeingPage() }}
                            disabled={currentChoosenHumanBeingPage === 1}
                            className="bg-orange-500 text-white py-2 px-4 rounded font-bold hover:bg-orange-600 transition-colors disabled:cursor-not-allowed"
                        >
                            Предыдущая
                        </button>
                        <span>Страница {currentChoosenHumanBeingPage} из {Math.ceil(totalChoosenHumanBeings / choosenHumanBeingsPerPage)}</span>
                        <button
                            onClick={() => { nextChoosenHumanBeingPage() }}
                            disabled={totalChoosenHumanBeings === 0 || currentChoosenHumanBeingPage === Math.ceil(totalChoosenHumanBeings / choosenHumanBeingsPerPage)}
                            className="bg-orange-500 text-white py-2 px-4 rounded font-bold hover:bg-orange-600 transition-colors disabled:cursor-not-allowed"
                        >
                            Следующая
                        </button>
                    </div>
                </div>

                <div className="bg-gray-800 p-6 rounded-lg shadow-lg">
                    <div className='flex justify-between items-center'>
                        <h2 className="text-2xl font-bold mb-4">Таблица Автомобилей</h2>
                        <CreateCarModal />
                    </div>
                    <table className="w-full mb-3">
                        <thead>
                            <tr className="border-b border-gray-700">
                                <th className="text-left py-2">ID</th>
                                <th className="text-left py-2">Название</th>
                                <th className="text-left py-2 w-64">Действия</th>
                            </tr>
                        </thead>
                        <tbody>
                            {cars.map((car) => (
                                <tr key={car.id} className="border-b border-gray-700">
                                    <td className="py-2">{car.id}</td>
                                    <td className="py-2">{car.name}</td>
                                    <td className="py-2 text-right">
                                        {(user.role === 'ADMIN' || car.userId === user.id) && (
                                            <>
                                                <UpdateCarModal car={car} />
                                                <button
                                                    onClick={() => { onCarDelete(car.id) }}
                                                    className="bg-red-500 text-white py-1 px-2 rounded text-sm hover:bg-red-600 transition-colors"
                                                >
                                                    Удалить
                                                </button>
                                            </>
                                        )}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    <div className="flex justify-between items-center">
                        <button
                            onClick={() => { prevCarPage() }}
                            disabled={currentCarPage === 1}
                            className="bg-orange-500 text-white py-2 px-4 rounded font-bold hover:bg-orange-600 transition-colors disabled:cursor-not-allowed"
                        >
                            Предыдущая
                        </button>
                        <span>Страница {currentCarPage} из {Math.ceil(totalCars / carsPerPage)}</span>
                        <button
                            onClick={() => { nextCarPage() }}
                            disabled={totalCars === 0 || currentCarPage === Math.ceil(totalCars / carsPerPage)}
                            className="bg-orange-500 text-white py-2 px-4 rounded font-bold hover:bg-orange-600 transition-colors disabled:cursor-not-allowed"
                        >
                            Следующая
                        </button>
                    </div>
                </div>

                <div className="bg-gray-800 p-6 rounded-lg shadow-lg">
                    <div className='flex justify-between items-center'>
                        <h2 className="text-2xl font-bold mb-4">Таблица Координат</h2>
                        <CreateCoordinatesModal />
                    </div>
                    <table className="w-full mb-3">
                        <thead>
                            <tr className="border-b border-gray-700">
                                <th className="text-left py-2">ID</th>
                                <th className="text-left py-2">x</th>
                                <th className="text-left py-2">y</th>
                                <th className="text-left py-2 w-64">Действия</th>
                            </tr>
                        </thead>
                        <tbody>
                            {coordinates.map((coordinate) => (
                                <tr key={coordinate.id} className="border-b border-gray-700">
                                    <td className="py-2">{coordinate.id}</td>
                                    <td className="py-2">{coordinate.x}</td>
                                    <td className="py-2">{coordinate.y}</td>
                                    <td className="py-2 text-right">
                                        {(user.role === 'ADMIN' || coordinate.userId === user.id) && (
                                            <>
                                                <UpdateCoordinatesModal coordinates={coordinate} />
                                                <button
                                                    onClick={() => { onCoordinatesDelete(coordinate.id) }}
                                                    className="bg-red-500 text-white py-1 px-2 rounded text-sm hover:bg-red-600 transition-colors"
                                                >
                                                    Удалить
                                                </button>
                                            </>
                                        )}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    <div className="flex justify-between items-center">
                        <button
                            onClick={() => { prevCoordinatesPage() }}
                            disabled={currentCoordinatesPage === 1}
                            className="bg-orange-500 text-white py-2 px-4 rounded font-bold hover:bg-orange-600 transition-colors disabled:cursor-not-allowed"
                        >
                            Предыдущая
                        </button>
                        <span>Страница {currentCoordinatesPage} из {Math.ceil(totalCoordinates / coordinatesPerPage)}</span>
                        <button
                            onClick={() => { nextCoordinatesPage() }}
                            disabled={totalCoordinates === 0 || currentCoordinatesPage === Math.ceil(totalCoordinates / coordinatesPerPage)}
                            className="bg-orange-500 text-white py-2 px-4 rounded font-bold hover:bg-orange-600 transition-colors disabled:cursor-not-allowed"
                        >
                            Следующая
                        </button>
                    </div>
                </div>

                {user !== null && user.role === 'ADMIN' && (
                    <div className="bg-gray-800 p-6 rounded-lg shadow-lg">
                        <h2 className="text-2xl font-bold mb-4">Таблица Запросов</h2>
                        <table className="w-full mb-3">
                            <thead>
                                <tr className="border-b border-gray-700">
                                    <th className="text-left py-2">ID</th>
                                    <th className="text-left py-2">Пользователь</th>
                                    <th className="text-left py-2 w-64">Действия</th>
                                </tr>
                            </thead>
                            <tbody>
                                {requests.map((request) => (
                                    <tr key={request.id} className="border-b border-gray-700">
                                        <td className="py-2">{request.id}</td>
                                        <td className="py-2">{request.username}</td>
                                        <td className="py-2 text-right">
                                            <button
                                                onClick={() => { onRequestApprove(request.id) }}
                                                className="bg-orange-500 text-white py-1 px-2 rounded text-sm hover:bg-orange-600 transition-colors"
                                            >
                                                Одобрить
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                        <div className="flex justify-between items-center">
                            <button
                                onClick={() => { prevRequestPage() }}
                                disabled={currentRequestPage === 1}
                                className="bg-orange-500 text-white py-2 px-4 rounded font-bold hover:bg-orange-600 transition-colors disabled:cursor-not-allowed"
                            >
                                Предыдущая
                            </button>
                            <span>Страница {currentRequestPage} из {Math.ceil(totalRequests / requestsPerPage)}</span>
                            <button
                                onClick={() => { nextRequestPage() }}
                                disabled={totalRequests === 0 || currentRequestPage === Math.ceil(totalRequests / requestsPerPage)}
                                className="bg-orange-500 text-white py-2 px-4 rounded font-bold hover:bg-orange-600 transition-colors disabled:cursor-not-allowed"
                            >
                                Следующая
                            </button>
                        </div>
                    </div>
                )}

                <div className="bg-gray-800 p-6 rounded-lg shadow-lg">
                    <div className='flex justify-between items-center'>
                        <h2 className="text-2xl font-bold mb-4">Таблица Импортов</h2>
                        <ImportModal />
                    </div>
                    <table className="w-full mb-3">
                        <thead>
                            <tr className="border-b border-gray-700">
                                <th className="text-left py-2">ID</th>
                                <th className="text-left py-2">Результат</th>
                                <th className="text-left py-2">Число объектов</th>
                                <th className="text-left py-2 w-64">ID пользователя</th>
                                <th className="text-left py-2 w-64">Действия</th>
                            </tr>
                        </thead>
                        <tbody>
                            {importAttempts.map((importAttempt) => (
                                <tr key={importAttempt.id} className="border-b border-gray-700">
                                    <td className="py-2">{importAttempt.id}</td>
                                    <td className="py-2">{importAttempt.isSuccessfull ? 'Положительный' : 'Отрицательный'}</td>
                                    <td className="py-2">{importAttempt.isSuccessfull ? importAttempt.objectCount : ''}</td>
                                    <td className="py-2">{importAttempt.user.id}</td>
                                    <td className="py-2">
                                        {importAttempt.isSuccessfull && (
                                            <button
                                                onClick={() => onFileDownload(importAttempt.filename)}
                                                className="bg-orange-500 text-white py-1 px-2 rounded text-sm hover:bg-red-600 transition-colors"
                                            >
                                                Скачать
                                            </button>
                                        )}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    <div className="flex justify-between items-center">
                        <button
                            onClick={() => { prevImportAttemptPage() }}
                            disabled={currentImportAttemptPage === 1}
                            className="bg-orange-500 text-white py-2 px-4 rounded font-bold hover:bg-orange-600 transition-colors disabled:cursor-not-allowed"
                        >
                            Предыдущая
                        </button>
                        <span>Страница {currentImportAttemptPage} из {Math.ceil(totalImportAttempts / importAttemptsPerPage)}</span>
                        <button
                            onClick={() => { nextImportAttemptPage() }}
                            disabled={totalImportAttempts === 0 || currentImportAttemptPage === Math.ceil(totalImportAttempts / importAttemptsPerPage)}
                            className="bg-orange-500 text-white py-2 px-4 rounded font-bold hover:bg-orange-600 transition-colors disabled:cursor-not-allowed"
                        >
                            Следующая
                        </button>
                    </div>
                </div>
            </div>


            <div className="p-6 mt-10 bg-gray-800 rounded-lg shadow-lg">
                <div className="flex justify-between items-center">
                    <h2 className="text-2xl font-bold mb-4">Таблица Людей</h2>
                    <CreateHumanBeingModal />
                </div>
                <table className="w-full mb-3">
                    <thead>
                        <tr className="border-b border-gray-700">
                            <th className="text-left py-2">ID</th>
                            <th className="text-left py-2">Имя</th>
                            <th className="text-left py-2">ID координат</th>
                            <th className="text-left py-2">Дата создания</th>
                            <th className="text-left py-2">Настоящий герой</th>
                            <th className="text-left py-2">Есть зубочистка</th>
                            <th className="text-left py-2">ID автомобиля</th>
                            <th className="text-left py-2 w-64">Настроение</th>
                            <th className="text-left py-2 w-64">Скорость влияния</th>
                            <th className="text-left py-2 w-64">Минуты ожидания</th>
                            <th className="text-left py-2 w-64">Тип оружия</th>
                            <th className="text-left py-2 w-64">Действия</th>
                        </tr>
                    </thead>
                    <tbody>
                        {humanBeings.map((humanBeing) => (
                            <tr key={humanBeing.id} className="border-b border-gray-700">
                                <td className="py-2">{humanBeing.id}</td>
                                <td className="py-2">{humanBeing.name}</td>
                                <td className="py-2">{humanBeing.coordinates.id}</td>
                                <td className="py-2">{humanBeing.creationDate}</td>
                                <td className="py-2">{humanBeing.realHero ? 'Да' : 'Нет'}</td>
                                <td className="py-2">{humanBeing.hasToothpick ? 'Да' : 'Нет'}</td>
                                <td className="py-2">{humanBeing.car === null ? '' : humanBeing.car.id}</td>
                                <td className="py-2">{humanBeing.mood}</td>
                                <td className="py-2">{humanBeing.impactSpeed}</td>
                                <td className="py-2">{humanBeing.minutesOfWaiting}</td>
                                <td className="py-2">{humanBeing.weaponType}</td>
                                <td className="py-2 text-right">
                                    {(user.role === 'ADMIN' || humanBeing.userId === user.id) && (
                                        <>
                                            <UpdateHumanBeingModal humanBeing={humanBeing} />
                                            <button
                                                onClick={() => onHumanBeingDelete(humanBeing.id)}
                                                className="bg-red-500 text-white py-1 px-2 rounded text-sm hover:bg-red-600 transition-colors"
                                            >
                                                Удалить
                                            </button>
                                        </>
                                    )}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                <div className="flex justify-between items-center">
                    <button
                        onClick={prevHumanBeingPage}
                        disabled={currentHumanBeingPage === 1}
                        className="bg-orange-500 text-white py-2 px-4 rounded font-bold hover:bg-orange-600 transition-colors disabled:cursor-not-allowed"
                    >
                        Предыдущая
                    </button>
                    <span>
                        Страница {currentHumanBeingPage} из{' '}
                        {Math.ceil(totalHumanBeings / humanBeingsPerPage)}
                    </span>
                    <button
                        onClick={nextHumanBeingPage}
                        disabled={totalHumanBeings === 0 || currentHumanBeingPage === Math.ceil(totalHumanBeings / humanBeingsPerPage)}
                        className="bg-orange-500 text-white py-2 px-4 rounded font-bold hover:bg-orange-600 transition-colors disabled:cursor-not-allowed"
                    >
                        Следующая
                    </button>
                </div>
            </div>


            <ToastContainer position="bottom-left" autoClose={3000} />
        </div>
    );
};

export default Home;