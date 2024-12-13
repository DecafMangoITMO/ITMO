import { zodResolver } from "@hookform/resolvers/zod";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import axios from "axios";
import axiosInstance from "../api/axios-instance";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const humanBeingSchema = z.object({
    name: z.string().min(1, { message: 'Название обязательно' }).max(32, 'Слишком длинное название'),
    coordinatesId: z.preprocess((num) => parseInt(z.string().parse(num), 10), z.number({ message: 'Идентификатор должен быть числом' }).gte(0, { message: 'Идентификатор должен быть положительным' })),
    realHero: z.string({ message: 'Свойство героя обязательно' }).min(1, { message: 'Свойство героя обязательно' }),
    hasToothpick: z.string({ message: 'Свойство владения зубочисткой обязательно' }).min(1, { message: 'Свойство владения зубочисткой обязательно' }),
    carId: z.preprocess((num) => num === '' ? undefined : parseInt(z.string().parse(num), 10),z.number({ message: 'Идентификатор должен быть числом' }).gte(0, { message: 'Идентификатор должен быть положительным' }).optional()),
    mood: z.string({ message: 'Настроение обязательно' }).min(1, { message: 'Настроение обязательно' }),
    impactSpeed: z.preprocess((num) => parseFloat(z.string().parse(num), 10), z.number({ message: 'Скорость влияния должна быть числом' }).gte(0, { message: 'Скорость влияния должна быть положительной' })),
    minutesOfWaiting: z.preprocess((num) => parseFloat(z.string().parse(num), 10), z.number({ message: 'Время ожидания должна быть числом' }).gte(0, { message: 'Время ожидания должна быть положительной' })),
    weaponType: z.string({ message: 'Тип оружия обязателен' }).min(1, { message: 'Тип оружия обязателен' }),
})

export const CreateHumanBeingModal = () => {
    const [isOpen, setIsOpen] = useState(false);

    const token = localStorage.getItem('custom-auth-token');
    const navigate = useNavigate();

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm({
        resolver: zodResolver(humanBeingSchema),
    });

    const onCreate = async (data) => {
        setIsOpen(false);

        data = {
            ...data,
            realHero: data.realHero === 'Да' ? true : false,
            hasToothpick: data.hasToothpick === 'Да' ? true : false,
        };

        try {
            const response = await axiosInstance.post(
                '/human-being',
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
                if (status === 401) {
                    toast.error('Пользователь не авторизован');
                    navigate('/auth');
                } else if (status === 404) {
                    toast.error('Проверьте корректность переданных идентификаторов');
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
                Добавить Человека
            </button>
            {isOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-gray-800 p-6 rounded-lg shadow-lg w-96">
                        <h2 className="text-xl font-bold mb-4 text-white">Добавить Человека</h2>
                        <input
                            type="text"
                            placeholder="Имя"
                            {...register('name')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {errors.name && <span className='text-red-500'>{errors.name.message}</span>}

                        <input
                            type="text"
                            placeholder="Идентификатор координат"
                            {...register('coordinatesId')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {errors.coordinatesId && <span className='text-red-500'>{errors.coordinatesId.message}</span>}

                        <p className="text-white mb-2">Настоящий герой:</p>
                        <div className="flex gap-4">
                            {["Да", "Нет"].map((answer) => (
                                <label key={answer} className="flex items-center text-white">
                                    <input
                                        type="radio"
                                        value={answer}
                                        {...register('realHero')}
                                        className="mr-2"
                                    />
                                    {answer}
                                </label>
                            ))}
                        </div>
                        {errors.realHero && <span className='text-red-500'>{errors.realHero.message}</span>}

                        <p className="text-white mb-2">Есть зубочистка:</p>
                        <div className="flex gap-4">
                            {["Да", "Нет"].map((answer) => (
                                <label key={answer} className="flex items-center text-white">
                                    <input
                                        type="radio"
                                        value={answer}
                                        {...register('hasToothpick')}
                                        className="mr-2"
                                    />
                                    {answer}
                                </label>
                            ))}
                        </div>
                        {errors.hasToothpick && <span className='text-red-500'>{errors.hasToothpick.message}</span>}

                        <input
                            type="text"
                            placeholder="Идентификатор автомобиля"
                            {...register('carId')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {errors.carId && <span className='text-red-500'>{errors.carId.message}</span>}

                        <select
                            {...register("mood")}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white"
                        >
                            <option value="">Выберите настроение</option>
                            {['SADNESS', 'SORROW', 'APATHY', 'CALM', 'FRENZY'].map((mood) => (
                                <option key={mood} value={mood}>
                                    {mood}
                                </option>
                            ))}
                        </select>
                        {errors.mood && <span className="text-red-500">{errors.mood.message}</span>}

                        <input
                            type="text"
                            placeholder="Скорость влияния"
                            {...register('impactSpeed')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {errors.impactSpeed && <span className='text-red-500'>{errors.impactSpeed.message}</span>}

                        <input
                            type="text"
                            placeholder="Минуты ожидания"
                            {...register('minutesOfWaiting')}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                        />
                        {errors.minutesOfWaiting && <span className='text-red-500'>{errors.minutesOfWaiting.message}</span>}

                        <select
                            {...register("weaponType")}
                            className="w-full p-3 mb-3 rounded bg-gray-700 text-white"
                        >
                            <option value="">Выберите тип оружия</option>
                            {['HAMMER', 'AXE', 'SHOTGUN', 'RIFLE', 'BAT'].map((mood) => (
                                <option key={mood} value={mood}>
                                    {mood}
                                </option>
                            ))}
                        </select>
                        {errors.weaponType && <span className="text-red-500">{errors.weaponType.message}</span>}

                        <div className="flex justify-end space-x-4">
                            <button
                                onClick={handleSubmit(onCreate)}
                                className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
                            >
                                Добавить
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

export const UpdateHumanBeingModal = ({ humanBeing }) => {
    const [isOpen, setIsOpen] = useState(false);

    const token = localStorage.getItem('custom-auth-token');
    const navigate = useNavigate();

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm({
        resolver: zodResolver(humanBeingSchema),
    });

    const onUpdate = async (data) => {
        setIsOpen(false);

        data = {
            ...data,
            realHero: data.realHero === 'Да' ? true : false,
            hasToothpick: data.hasToothpick === 'Да' ? true : false,
        };

        try {
            const response = await axiosInstance.patch(
                '/human-being/' + humanBeing.id,
                data,
                {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    }
                },
            );
            toast.success('Человек успешно обновлен');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const status = error.response.status;
                if (status === 401) {
                    toast.error('Пользователь не авторизован');
                    navigate('/auth');
                } else if (status === 404) {
                    toast.error('Проверьте корректность переданных идентификаторов');
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
                    <h2 className="text-xl font-bold mb-4 text-white text-left">Редактировать Человека</h2>
                    <input
                        type="text"
                        placeholder="Имя"
                        defaultValue={humanBeing.name}
                        {...register('name')}
                        className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                    />
                    {errors.name && <span className='text-red-500'>{errors.name.message}</span>}

                    <input
                        type="text"
                        placeholder="Идентификатор координат"
                        defaultValue={humanBeing.coordinates.id}
                        {...register('coordinatesId')}
                        className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                    />
                    {errors.coordinatesId && <span className='text-red-500'>{errors.coordinatesId.message}</span>}

                    <p className="text-white mb-2 text-left">Настоящий герой:</p>
                    <div className="flex gap-4">
                        {["Да", "Нет"].map((answer) => (
                            <label key={answer} className="flex items-center text-white">
                                <input
                                    type="radio"
                                    value={answer}
                                    defaultChecked={answer === humanBeing.realHero}
                                    {...register('realHero')}
                                    className="mr-2"
                                />
                                {answer}
                            </label>
                        ))}
                    </div>
                    {errors.realHero && <span className='text-red-500'>{errors.realHero.message}</span>}

                    <p className="text-white mb-2 text-left">Есть зубочистка:</p>
                    <div className="flex gap-4">
                        {["Да", "Нет"].map((answer) => (
                            <label key={answer} className="flex items-center text-white">
                                <input
                                    type="radio"
                                    value={answer}
                                    defaultChecked={answer === humanBeing.hasToothpick}
                                    {...register('hasToothpick')}
                                    className="mr-2"
                                />
                                {answer}
                            </label>
                        ))}
                    </div>
                    {errors.hasToothpick && <span className='text-red-500'>{errors.hasToothpick.message}</span>}

                    <input
                        type="text"
                        placeholder="Идентификатор автомобиля"
                        defaultValue={humanBeing.car === null ? '' : humanBeing.car.id}
                        {...register('carId')}
                        className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                    />
                    {errors.carId && <span className='text-red-500'>{errors.carId.message}</span>}

                    <select
                        {...register("mood")}
                        className="w-full p-3 mb-3 rounded bg-gray-700 text-white"
                    >
                        <option value="">Выберите настроение</option>
                        {['SADNESS', 'SORROW', 'APATHY', 'CALM', 'FRENZY'].map((mood) => (
                            <option key={mood} value={mood} defaultChecked={mood === humanBeing.mood}>
                                {mood}
                            </option>
                        ))}
                    </select>
                    {errors.mood && <span className="text-red-500">{errors.mood.message}</span>}

                    <input
                        type="text"
                        placeholder="Скорость влияния"
                        defaultValue={humanBeing.impactSpeed}
                        {...register('impactSpeed')}
                        className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                    />
                    {errors.impactSpeed && <span className='text-red-500'>{errors.impactSpeed.message}</span>}

                    <input
                        type="text"
                        placeholder="Минуты ожидания"
                        defaultValue={humanBeing.minutesOfWaiting}
                        {...register('minutesOfWaiting')}
                        className="w-full p-3 mb-3 rounded bg-gray-700 text-white placeholder-gray-400"
                    />
                    {errors.minutesOfWaiting && <span className='text-red-500'>{errors.minutesOfWaiting.message}</span>}

                    <select
                        {...register("weaponType")}
                        className="w-full p-3 mb-3 rounded bg-gray-700 text-white"
                    >
                        <option value="">Выберите настроение</option>
                        {['HAMMER', 'AXE', 'SHOTGUN', 'RIFLE', 'BAT'].map((mood) => (
                            <option key={mood} value={mood} defaultChecked={mood === humanBeing.weaponType}>
                                {mood}
                            </option>
                        ))}
                    </select>
                    {errors.weaponType && <span className="text-red-500">{errors.weaponType.message}</span>}

                    <div className="flex justify-end space-x-4">
                        <button
                            onClick={handleSubmit(onUpdate)}
                            className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
                        >
                            Обновить
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