import axios from "axios";
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../../provider/AuthProvider";
import "../../create-modal.css"

const AdvancedModal = ({open, setOpen}) => {
    const {setUser} = useAuth();
    const navigate = useNavigate();

    const [impactSpeed, setImpactSpeed] = useState('')
    const [impactSpeedIsValid, setImpactSpeedIsValid] = useState(false)

    const [minutesOfWaiting, setMinutesOfWaiting] = useState('')
    const [minutesOfWaitingIsValid, setMinutesOfWaitingIsValid] = useState(false)

    const [resultMessage, setResultMessage] = useState('');

    const deleteByImpactSpeed = (event) => {
        event.preventDefault()

        axios.delete(
            `/human-being/impact-speed?impactSpeed=${impactSpeed}`,
            {}
        ).then(response => {
                setResultMessage('Human beings were deleted')
            }
        ).catch(error => {
            if (error.response.status === 401) {
                setUser(null);
                navigate("/");
            } else {
                console.log(error.response.data.message())
            }
        })
    }

    const getRandomWithMaxMinutesOfWaiting = (event) => {
        event.preventDefault()

        axios.get(
            '/human-being/random/max-minutes-of-waiting',
            {}
        ).then(response => {
                const humanBeing = response.data

                setResultMessage(
                    <div>
                        <table>
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
                            <tr>
                                <td>{humanBeing.id}</td>
                                <td>{humanBeing.name}</td>
                                <td>{humanBeing.coordinates.x}</td>
                                <td>{humanBeing.coordinates.y}</td>
                                <td>{humanBeing.creationDate}</td>
                                <td>{humanBeing.realHero}</td>
                                <td>{humanBeing.hasToothpick}</td>
                                <td>{humanBeing.car !== null ? humanBeing.car.name : null}</td>
                                <td>{humanBeing.mood}</td>
                                <td>{humanBeing.impactSpeed}</td>
                                <td>{humanBeing.minutesOfWaiting}</td>
                                <td>{humanBeing.weaponType}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                )
            }
        ).catch(error => {
            if (error.response.status === 401) {
                setUser(null);
                navigate("/");
            } else if (error.response.status === 404) {
                setResultMessage(error.response.data.message)
            } else {
                console.log(error)
            }
        })
    }

    const getWithMinutesOfWaitingLess = (event) => {
        event.preventDefault()

        axios.get(
            `/human-being/less-minutes-of-waiting/${minutesOfWaiting}`,
            {}
        ).then(response => {
                const humanBeings = response.data

                setResultMessage(
                    <div>
                        <table>
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
                            {humanBeings.map(humanBeing => (
                                <tr key={humanBeing.id}>
                                    <td>{humanBeing.id}</td>
                                    <td>{humanBeing.name}</td>
                                    <td>{humanBeing.coordinates.x}</td>
                                    <td>{humanBeing.coordinates.y}</td>
                                    <td>{humanBeing.creationDate}</td>
                                    <td>{humanBeing.realHero}</td>
                                    <td>{humanBeing.hasToothpick}</td>
                                    <td>{humanBeing.car !== null ? humanBeing.car.name : null}</td>
                                    <td>{humanBeing.mood}</td>
                                    <td>{humanBeing.impactSpeed}</td>
                                    <td>{humanBeing.minutesOfWaiting}</td>
                                    <td>{humanBeing.weaponType}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )
            }
        ).catch(error => {
            if (error.response.status === 401) {
                setUser(null);
                navigate("/");
            } else {
                console.log(error.response.data.message())
            }
        })
    }

    const deleteAllWithoutToothpick = (event) => {
        event.preventDefault()

        axios.delete(
            `/human-being/has-toothpick`,
            {}
        ).then(response => {
                setResultMessage('Human beings without toothpicks deleted')
            }
        ).catch(error => {
            if (error.response.status === 401) {
                setUser(null);
                navigate("/");
            } else {
                console.log(error)
            }
        })
    }

    const giveLadaKalina = (event) => {
        event.preventDefault()

        axios.patch(
            "/human-being/lada-kalina",
            {},
            {}
        ).then(response => {
                setResultMessage('Human beings without cars were given Lada Kalina')
            }
        ).catch(error => {
            if (error.response.status === 401) {
                setUser(null);
                navigate("/");
            } else {
                console.log(error)
            }
        })
    }


    return (
        <div>
            <button className="toggle-button" onClick={() => {
                setOpen(open === "advanced" ? "" : "advanced")
            }}>Advanced
            </button>
            {open === "advanced" && (
                <div className='modal-content'>
                    <span className="form-group">
                    <button className='submit-button' onClick={deleteByImpactSpeed} disabled={!impactSpeedIsValid}>Delete by impact speed</button>
                    <input type='text' value={impactSpeed} onChange={(event) => {
                        const value = event.target.value
                        if (value.match(/^[1-9][0-9]*$/) !== null) {
                            setImpactSpeedIsValid(true)
                        } else {
                            setImpactSpeedIsValid(false)
                        }
                        setImpactSpeed(event.target.value)
                    }}/>
                    </span>
                    <span className="form-group">
                        <button className='submit-button' onClick={getRandomWithMaxMinutesOfWaiting}>Get random with max impact speed</button>
                    </span>
                    <span className="form-group">
                        <button className='submit-button' onClick={getWithMinutesOfWaitingLess} disabled={!minutesOfWaitingIsValid}>Get with minutes of waiting less than</button>
                        <input type='text' value={minutesOfWaiting} onChange={(event) => {
                            const value = event.target.value
                            if (value.match(/^[1-9][0-9]*$/) !== null) {
                                setMinutesOfWaitingIsValid(true)
                            } else {
                                setMinutesOfWaitingIsValid(false)
                            }
                            setMinutesOfWaiting(event.target.value)
                        }}/>
                    </span>
                    <span className="form-group">
                        <button className='submit-button' onClick={deleteAllWithoutToothpick}>Delete all without toothpick</button>
                    </span>
                    <span className="form-group">
                        <button className='submit-button'onClick={giveLadaKalina}>Add Lada Kalina</button>
                    </span>
                    <span>
                        <p className="result-message">{resultMessage}</p>
                    </span>
                </div>
            )}
        </div>
    )
}

export default AdvancedModal