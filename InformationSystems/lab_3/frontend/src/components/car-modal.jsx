import { zodResolver } from "@hookform/resolvers/zod";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import axios from "axios";
import axiosInstance from "../api/axios-instance";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const carSchema = z.object({
    name: z.string().min(1, { message: 'Название обязательно' }).max(32, 'Слишком длинное название'),
})

export const CreateCarModal = () => {
    const [isOpen, setIsOpen] = useState(false);
    
    const token = localStorage.getItem('custom-auth-token');
    const navigate = useNavigate();

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm({
        resolver: zodResolver(carSchema),
    });

    const onCreate = async (data) => {
        setIsOpen(false);

        try {
            const response = await axiosInstance.post(
                '/car',
                data,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            toast.success('Автомобиль успешно создан');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response.status;
                if (status === 400)
                    toast.error('Автомобиль с таким названием уже существует');
                else if (status === 401) {
                    toast.error('Пользователь не авторизован');
                    navigate('/auth');
                } else
                    toast.error('Нет ответа от сервера');
            } else {
                toast.error('Произошла ошибка');
            }
        };
    }

    return (
        <>
            <button
                onClick={() => { setIsOpen(true) }}
                className="mt-4 px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600"
            >
                Добавить автомобиль
            </button>
            {isOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-gray-800 p-6 rounded-lg shadow-lg w-96">
                        <h2 className="text-xl font-bold mb-4 text-white">Добавить автомобиль</h2>
                        <input
                            type="text"
                            placeholder="Название"
                            {...register('name')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {errors.name && <span className='text-red-500'>{errors.name.message}</span>}
                    
                        <div className="flex justify-end space-x-4">
                            <button
                                onClick={handleSubmit(onCreate)}
                                className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
                            >
                                Добавить
                            </button>
                            <button
                                onClick={() => {setIsOpen(false)}}
                                className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                            >
                                Отмена
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </>
    )
}

export const UpdateCarModal = ({ car }) => {
    const [isOpen, setIsOpen] = useState(false);
    
    const token = localStorage.getItem('custom-auth-token');
    const navigate = useNavigate();

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm({
        resolver: zodResolver(carSchema),
    });

    const onUpdate = async (data) => {
        setIsOpen(false);

        try {
            const response = await axiosInstance.patch(
                '/car/' + car.id,
                data,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            toast.success('Автомобиль успешно обновлен');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response.status;
                if (status === 400)
                    toast.error('Автомобиль с таким названием уже существует');
                else if (status === 401) {
                    toast.error('Пользователь не авторизован');
                    navigate('/auth');
                } else
                    toast.error('Нет ответа от сервера');
            } else {
                toast.error('Произошла ошибка');
            }
        };
    }

    return (
        <>
            <button
                onClick={() => { setIsOpen(true) }}
                className="text-white py-1 px-2 rounded text-sm transition-colors bg-orange-500 text-white rounded hover:bg-orange-600 mr-1"
            >
                Редактировать
            </button>
            {isOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-gray-800 p-6 rounded-lg shadow-lg w-96">
                        <h2 className="text-xl font-bold mb-4 text-white text-left">Редактировать автомобиль</h2>
                        <input
                            type="text"
                            placeholder="Название"
                            defaultValue={car.name}
                            {...register('name')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {errors.name && <span className='text-red-500'>{errors.name.message}</span>}
                    
                        <div className="flex justify-end space-x-4">
                            <button
                                onClick={handleSubmit(onUpdate)}
                                className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
                            >
                                Обновить
                            </button>
                            <button
                                onClick={() => {setIsOpen(false)}}
                                className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                            >
                                Отмена
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </>
    )
}