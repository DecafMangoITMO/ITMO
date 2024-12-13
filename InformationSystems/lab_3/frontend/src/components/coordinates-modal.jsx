import { zodResolver } from "@hookform/resolvers/zod";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import axios from "axios";
import axiosInstance from "../api/axios-instance";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const coordinatesSchema = z.object({
    x: z.preprocess((num) => parseFloat(z.string().parse(num), 10), z.number({ message: 'Координата x должна быть числом' }).gte(0, { message: 'Координата x максимум 647' })),
    y: z.preprocess((num) => parseInt(z.string().parse(num), 10), z.number({ message: 'Координата y должна быть числом' }).gte(0, { message: 'Координата y максимум 123' })),
})

export const CreateCoordinatesModal = () => {
    const [isOpen, setIsOpen] = useState(false);
    
    const token = localStorage.getItem('custom-auth-token');
    const navigate = useNavigate();

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm({
        resolver: zodResolver(coordinatesSchema),
    });

    const onCreate = async (data) => {
        setIsOpen(false);

        try {
            const response = await axiosInstance.post(
                '/coordinates',
                data,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            toast.success('Координаты успешно созданы');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response.status;
                if (status === 400)
                    toast.error('Такие координаты уже существуют');
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
                Добавить координаты
            </button>
            {isOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-gray-800 p-6 rounded-lg shadow-lg w-96">
                        <h2 className="text-xl font-bold mb-4 text-white">Добавить координаты</h2>
                        <input
                            type="text"
                            placeholder="x"
                            {...register('x')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {errors.x && <span className='text-red-500'>{errors.x.message}</span>}
                    
                        <input
                            type="text"
                            placeholder="y"
                            {...register('y')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {errors.y && <span className='text-red-500'>{errors.y.message}</span>}

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

export const UpdateCoordinatesModal = ({ coordinates }) => {
    const [isOpen, setIsOpen] = useState(false);
    
    const token = localStorage.getItem('custom-auth-token');
    const navigate = useNavigate();

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm({
        resolver: zodResolver(coordinatesSchema),
    });

    const onUpdate = async (data) => {
        setIsOpen(false);

        try {
            const response = await axiosInstance.patch(
                '/coordinates/' + coordinates.id,
                data,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            toast.success('Координаты успешно обновлены');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response.status;
                if (status === 400)
                    toast.error('Такие координаты уже существуют');
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
                        <h2 className="text-xl font-bold mb-4 text-white text-left">Редактировать кординаты</h2>
                        <input
                            type="text"
                            placeholder="x"
                            defaultValue={coordinates.x}
                            {...register('x')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {errors.x && <span className='text-red-500'>{errors.x.message}</span>}
                    
                        <input
                            type="text"
                            placeholder="y"
                            defaultValue={coordinates.y}
                            {...register('y')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {errors.y && <span className='text-red-500'>{errors.y.message}</span>}

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