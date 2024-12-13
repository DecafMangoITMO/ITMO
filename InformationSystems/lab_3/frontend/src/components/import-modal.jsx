import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import axios from 'axios';
import axiosInstance from '../api/axios-instance';
import { toast } from 'react-toastify';

const ACCEPTED_IMAGE_TYPES = ['text/plain'];

const importSchema = z.object({
    file: z
        .any()
        // .refine((files) => files?.[0]?.size >= MAX_FILE_SIZE, `Максимальный размер 5MB.`)
        .refine(
            (files) => ACCEPTED_IMAGE_TYPES.includes(files?.[0]?.type),
            "Принимается только файл формата .txt"
        ),
})

const ImportModal = () => {

    const [isOpen, setIsOpen] = useState(false);
    
    const token = localStorage.getItem('custom-auth-token');
    const navigate = useNavigate();

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm({
        resolver: zodResolver(importSchema),
    });

    const onImport = async (data) => {
        setIsOpen(false);

        const form = new FormData();
        form.append('file', data.file[0]);

        try {
            const response = await axiosInstance.post(
                '/import',
                form,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'multipart/form-data',
                    }
                },
            );
            toast.success('Файл отправлен');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response.status;
                if (status === 400)
                    toast.error('Невалидный файл');
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
                Импортировать
            </button>
            {isOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-gray-800 p-6 rounded-lg shadow-lg w-96">
                        <h2 className="text-xl font-bold mb-4 text-white">Импортировать</h2>
                        <input
                            type="file"
                            {...register('file')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {errors.file && <span className='text-red-500'>{errors.file.message}</span>}

                        <div className="flex justify-end space-x-4">
                            <button
                                onClick={handleSubmit(onImport)}
                                className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
                            >
                                Импортировать
                            </button>
                            <button
                                onClick={() => { setIsOpen(false) }}
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

export default ImportModal;