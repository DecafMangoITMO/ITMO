import React from 'react'

const NotFound = () => {
    return (
        <div className="min-h-screen bg-gray-900 flex items-center justify-center text-white">
            <div className="text-center">
                <h1 className="text-6xl font-bold mb-4">404</h1>
                <p className="text-2xl mb-6">Страница не найдена</p>
                <p className="mb-8">Извините, но запрашиваемая страница не существует.</p>
                <a href="/home" className="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600 transition duration-300">
                    Вернуться на главную
                </a>
            </div>
        </div>
    )
}

export default NotFound;