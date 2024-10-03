import {useEffect, useRef, useState} from "react";
import "../create-modal.css"
import "../home.css"

const CanvasField = ({open, setOpen, humanBeings}) => {
    const canvasRef = useRef(null);
    const [foundHumanBeings, setFoundHumanBeings] = useState([]);

    const generateColorFromId = (id) => {
        const hex = ((id * 123456) % 16777215).toString(16);
        return `#${hex.padStart(6, '0')}`;
    };

    const handleCanvasClick = (event) => {
        const canvas = canvasRef.current;
        const rect = canvas.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;

        let newFoundHumanBeings = []

        humanBeings.forEach(humanBeing => {
            const {x: humanX, y: humanY} = humanBeing.coordinates;

            // Проверяем, попадает ли клик в область объекта
            if (x >= humanX && x <= humanX + 10 && y >= humanY && y <= humanY + 10) {
                newFoundHumanBeings.push(humanBeing);
            }
        });

        setFoundHumanBeings(newFoundHumanBeings);
    };

    useEffect(() => {
        if (!open) return; // Если окно не открыто, ничего не делаем
        const canvas = canvasRef.current;

        if (!canvas) {
            console.error('Canvas is null');
            return;
        }

        const context = canvas.getContext('2d');

        if (!context) {
            console.error('Failed to get canvas context');
            return;
        }

        context.clearRect(0, 0, canvas.width, canvas.height);

        humanBeings.forEach(humanBeing => {
            context.color = generateColorFromId(humanBeing.userId);
            const x = Math.max(0, Math.min(humanBeing.coordinates.x, canvas.width - 10));
            const y = Math.max(0, Math.min(humanBeing.coordinates.y, canvas.height - 10));
            context.fillRect(x, y, 10, 10);
        })
    }, [humanBeings, open])

    return (
        <div>
            <button className="toggle-button" onClick={() => {
                setOpen(open === "canvas" ? "" : "canvas")
            }}>Canvas
            </button>
            {open === "canvas" &&
                <div className="modal-content">
                    <canvas ref={canvasRef} width={800} height={600}
                            onClick={handleCanvasClick}/>
                    <h2>Found HumanBeings</h2>
                    <table className="styled-table">
                        <thead>
                        <tr>
                            <th>id</th>
                            <th>name</th>
                            <th>x</th>
                            <th>y</th>
                            <th>creation date</th>
                            <th>real hero</th>
                            <th>has toothpick</th>
                            <th>car</th>
                            <th>mood</th>
                            <th>impact speed</th>
                            <th>minutes of waiting</th>
                            <th>weapon type</th>
                        </tr>
                        </thead>
                        <tbody>
                        {foundHumanBeings.map(humanBeing => (
                            <tr key={humanBeing.id}>
                                <td>{humanBeing.id}</td>
                                <td>{humanBeing.name}</td>
                                <td>{humanBeing.coordinates.x}</td>
                                <td>{humanBeing.coordinates.y}</td>
                                <td>{humanBeing.creationDate}</td>
                                <td>{humanBeing.realHero ? 'true' : 'false'}</td>
                                <td>{humanBeing.hasToothpick ? 'true' : 'false'}</td>
                                <td>{humanBeing.car !== null ? humanBeing.car.name : null}</td>
                                <td>{humanBeing.mood}</td>
                                <td>{humanBeing.impactSpeed}</td>
                                <td>{humanBeing.minutesOfWaiting === null ? 'null' : humanBeing.minutesOfWaiting}</td>
                                <td>{humanBeing.weaponType}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>}
        </div>
    )

}

export default CanvasField;