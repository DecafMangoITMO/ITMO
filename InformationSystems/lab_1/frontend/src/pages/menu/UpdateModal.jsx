import {useAuth} from "../../provider/AuthProvider";
import {useNavigate} from "react-router-dom";
import {useState} from "react";
import {MoodTypes, ObjectTypes, WeaponTypes} from "./types";
import axios from "axios";
import "../../create-modal.css"

const UpdateModal = ({open, setOpen, humanBeings, cars, coordinates}) => {
    const {setUser} = useAuth();
    const navigate = useNavigate();

    const [selectedId, setSelectedId] = useState('');
    const [objectFound, setObjectFound] = useState(false);
    const [searchMessage, setSearchMessage] = useState('');

    const [objectType, setObjectType] = useState(ObjectTypes.HUMAN_BEING);
    const [updateHumanBeing, setUpdateHumanBeing] = useState({
        name: "",
        coordinatesId: "",
        realHero: false,
        hasToothpick: false,
        carId: '',
        mood: MoodTypes.CALM,
        impactSpeed: '',
        minutesOfWaiting: '',
        weaponType: WeaponTypes.AXE,
    });
    const [updateHumanBeingErrors, setUpdateHumanBeingErrors] = useState({
        name: "",
        coordinatesId: "",
        carId: "",
        impactSpeed: "",
        minutesOfWaiting: "",
    })

    const [updateCar, setUpdateCar] = useState({
        name: "",
    })
    const [updateCarErrors, setUpdateCarErrors] = useState({
        name: "",
    })

    const [updateCoordinates, setUpdateCoordinates] = useState({
        x: "",
        y: "",
    })
    const [updateCoordinatesErrors, setUpdateCoordinatesErrors] = useState({
        x: "",
        y: "",
    })


    const [resultMessage, setResultMessage] = useState('');

    const renderForm = () => {
        switch (objectType) {
            case ObjectTypes.HUMAN_BEING:
                return (
                    <form className="modal-form">
                        <h2>HumanBeing creation:</h2>
                        <div className="form-group">
                            <label>Name:</label>
                            <input type='text' value={updateHumanBeing.name} onChange={(event) => {
                                setUpdateHumanBeing({
                                    ...updateHumanBeing,
                                    name: event.target.value,
                                })
                            }}/>
                            {updateHumanBeingErrors.name && <span>{updateHumanBeingErrors.name}</span>}
                        </div>
                        <div className="form-group">
                            <label>Coordinates ID:</label>
                            <input type='text' value={updateHumanBeing.coordinatesId}
                                   onChange={(event) => {
                                       setUpdateHumanBeing({
                                           ...updateHumanBeing,
                                           coordinatesId: event.target.value,
                                       })
                                   }}/>
                            {updateHumanBeingErrors.coordinatesId &&
                                <span>{updateHumanBeingErrors.coordinatesId}</span>}
                        </div>
                        <div className="form-group">
                            <label>
                                <input type='checkbox' checked={updateHumanBeing.realHero}
                                       onChange={(event) => {
                                           setUpdateHumanBeing({
                                               ...updateHumanBeing,
                                               realHero: event.target.value,
                                           })
                                       }}/>
                                Real Hero
                            </label>
                        </div>
                        <div className="form-group">
                            <label>
                                <input type='checkbox' checked={updateHumanBeing.hasToothpick}
                                       onChange={(event) => {
                                           setUpdateHumanBeing({
                                               ...updateHumanBeing,
                                               hasToothpick: event.target.value,
                                           })
                                       }}/>
                                Has Toothpick
                            </label>
                        </div>
                        <div className="form-group">
                            <label>Car ID:</label>
                            <input type='text' value={updateHumanBeing.carId} onChange={(event) => {
                                setUpdateHumanBeing({
                                    ...updateHumanBeing,
                                    carId: event.target.value,
                                })
                            }}/>
                            {updateHumanBeingErrors.carId && <span>{updateHumanBeingErrors.carId}</span>}
                        </div>
                        <div className="form-group">
                            <label>Mood:</label>
                            <select value={updateHumanBeing.mood} onChange={(event) => {
                                setUpdateHumanBeing({
                                    ...updateHumanBeing,
                                    mood: event.target.value,
                                })
                            }}>
                                <option value={MoodTypes.SADNESS}>{MoodTypes.SADNESS}</option>
                                <option value={MoodTypes.SORROW}>{MoodTypes.SORROW}</option>
                                <option value={MoodTypes.APATHY}>{MoodTypes.APATHY}</option>
                                <option value={MoodTypes.CALM}>{MoodTypes.CALM}</option>
                                <option value={MoodTypes.FRENZY}>{MoodTypes.FRENZY}</option>
                            </select>
                        </div>
                        <div className="form-group">
                            <label>Impact Speed:</label>
                            <input type='text' value={updateHumanBeing.impactSpeed}
                                   onChange={(event) => {
                                       setUpdateHumanBeing({
                                           ...updateHumanBeing,
                                           impactSpeed: event.target.value,
                                       })
                                   }}/>
                            {updateHumanBeingErrors.impactSpeed && <span>{updateHumanBeingErrors.impactSpeed}</span>}
                        </div>
                        <div className="form-group">
                            <label>Minutes of Waiting:</label>
                            <input type='text' value={updateHumanBeing.minutesOfWaiting}
                                   onChange={(event) => {
                                       setUpdateHumanBeing({
                                           ...updateHumanBeing,
                                           minutesOfWaiting: event.target.value,
                                       })
                                   }}/>
                            {updateHumanBeingErrors.minutesOfWaiting &&
                                <span>{updateHumanBeingErrors.minutesOfWaiting}</span>}
                        </div>
                        <div className="form-group">
                            <label>Weapon Type:</label>
                            <select value={updateHumanBeing.weaponType} onChange={(event) => {
                                setUpdateHumanBeing({
                                    ...updateHumanBeing,
                                    weaponType: event.target.value,
                                })
                            }}>
                                <option value={WeaponTypes.HAMMER}>{WeaponTypes.HAMMER}</option>
                                <option value={WeaponTypes.AXE}>{WeaponTypes.AXE}</option>
                                <option value={WeaponTypes.SHOTGUN}>{WeaponTypes.SHOTGUN}</option>
                                <option value={WeaponTypes.RIFLE}>{WeaponTypes.RIFLE}</option>
                                <option value={WeaponTypes.BAT}>{WeaponTypes.BAT}</option>
                            </select>
                        </div>
                    </form>
                )
            case ObjectTypes.CAR:
                return (
                    <form className="modal-form">
                        <h2>Car creation:</h2>
                        <div className="form-group">
                            <label>Name:</label>
                            <input type='text' value={updateCar.name} onChange={(event) => {
                                setUpdateCar({
                                    ...updateCar,
                                    name: event.target.value,
                                })
                            }}/>
                            {updateCarErrors.name && <span>{updateCarErrors.name}</span>}
                        </div>
                    </form>
                )
            case ObjectTypes.COORDINATES:
                return (
                    <form className="modal-form">
                        <h2>Coordinates creation:</h2>
                        <div className="form-group">
                            <label>X:</label>
                            <input type='text' value={updateCoordinates.x} onChange={(event) => {
                                setUpdateCoordinates({
                                    ...updateCoordinates,
                                    x: event.target.value,
                                })
                            }}/>
                            {updateCoordinatesErrors.x && <span>{updateCoordinatesErrors.x}</span>}
                        </div>
                        <div className="form-group">
                            <label>Y:</label>
                            <input type='text' value={updateCoordinates.y} onChange={(event) => {
                                setUpdateCoordinates({
                                    ...updateCoordinates,
                                    y: event.target.value,
                                })
                            }}/>
                            {updateCoordinatesErrors.y && <span>{updateCoordinatesErrors.y}</span>}
                        </div>
                    </form>
                )
            default:
                return null
        }
    }

    const searchForObject = () => {
        if (selectedId.match(/^[1-9][0-9]*$/) === null) {
            setSearchMessage("Invalid id");
            setObjectFound(false);
            return;
        }

        const objectId = Number.parseInt(selectedId)
        switch (objectType) {
            case ObjectTypes.HUMAN_BEING:
                const targetHumanBeing = humanBeings.find(humanBeing => humanBeing.id === objectId);
                console.log(targetHumanBeing)
                if (targetHumanBeing) {
                    setSearchMessage("")
                    setUpdateHumanBeing({
                        name: targetHumanBeing.name,
                        coordinatesId: targetHumanBeing.coordinates.id,
                        realHero: targetHumanBeing.realHero,
                        hasToothpick: targetHumanBeing.hasToothpick,
                        carId: targetHumanBeing.car === null ? "" : targetHumanBeing.car.id,
                        mood: targetHumanBeing.mood,
                        impactSpeed: targetHumanBeing.impactSpeed,
                        minutesOfWaiting: targetHumanBeing.minutesOfWaiting,
                        weaponType: targetHumanBeing.weaponType
                    });
                    setObjectFound(true)
                } else {
                    setObjectFound(false)
                    setSearchMessage("HumanBeing not found");
                }
                break;
            case ObjectTypes.CAR:
                const targetCar = cars.find(car => car.id === objectId);
                if (targetCar) {
                    setSearchMessage("")
                    setUpdateCar(targetCar);
                    setObjectFound(true)
                } else {
                    setObjectFound(false)
                    setSearchMessage("Car not found");
                }
                break;
            case ObjectTypes.COORDINATES:
                const targetCoordinates = coordinates.find(coordinates1 => coordinates1.id === objectId);
                console.log(objectId);
                if (targetCoordinates) {
                    setSearchMessage("")
                    setUpdateCoordinates(targetCoordinates);
                    setObjectFound(true)
                } else {
                    setObjectFound(false)
                    setSearchMessage("Coordinates not found");
                }
                break;
            default:
        }
    }


    const validateInput = () => {
        let valid = true;
        switch (objectType) {
            case ObjectTypes.HUMAN_BEING:
                let newCreateHumanBeingErrors = {};
                if (updateHumanBeing.name.match(/^\s*$/) !== null) {
                    newCreateHumanBeingErrors.name = "Name must be between 6 and 10 characters";
                    valid = false
                }

                if (String(updateHumanBeing.coordinatesId).match(/^[1-9][0-9]*$/) === null) {
                    newCreateHumanBeingErrors.coordinatesId = "CoordinatesId must be positive integer"
                    valid = false;
                }

                if (String(updateHumanBeing.carId).match(/^[1-9][0-9]*$/) === null) {
                    newCreateHumanBeingErrors.carId = "CarId must be positive integer";
                    valid = false;
                }

                if (String(updateHumanBeing.impactSpeed).match(/^-?\d+(\.\d+)?$/) === null) {
                    newCreateHumanBeingErrors.impactSpeed = "ImpactSpeed must be a number";
                    valid = false;
                }

                if (String(updateHumanBeing.minutesOfWaiting).match(/^\s*$|^-?\d+(\.\d+)?$/) === null) {
                    newCreateHumanBeingErrors.minutesOfWaiting = "Minutes must be a number or empty";
                    valid = false;
                }

                setUpdateHumanBeingErrors(newCreateHumanBeingErrors);
                return valid;
            case ObjectTypes.CAR:
                let newCreateCarErrors = {};
                if (updateCar.name.match(/^\s+$/) !== null) {
                    newCreateCarErrors.name = "Name must not be blank";
                    valid = false;
                }
                setUpdateCarErrors(newCreateCarErrors);
                return valid;
            case ObjectTypes.COORDINATES:
                let newUpdateCoordinatesErrors = {};
                if (String(updateCoordinates.x).match(/^-?\d+(\.\d+)?$/) === null || (String(updateCoordinates.x).match(/^-?\d+(\.\d+)?$/) !== null && Number.parseFloat(updateCoordinates.x) > 647)) {
                    newUpdateCoordinatesErrors.x = "x must real number not more than 647";
                    valid = false;
                }

                if (String(updateCoordinates.y).match(/^-?\d+$/) === null || (String(updateCoordinates.y).match(/^-?\d+$/) !== null && Number.parseInt(updateCoordinates.x) > 123)) {
                    newUpdateCoordinatesErrors.y = "y must be integer not more than 123";
                    valid = false;
                }

                setUpdateCoordinatesErrors(newUpdateCoordinatesErrors);
                return valid;
        }
    }

    const sendRequest = () => {
        if (validateInput()) {
            switch (objectType) {
                case ObjectTypes.HUMAN_BEING:
                    axios.patch(
                        `http://localhost:8080/human-being/${selectedId}`,
                        {
                            ...updateHumanBeing,
                            realHero: !!updateHumanBeing.realHero,
                            hasToothpick: !!updateHumanBeing.hasToothpick,
                        },
                        {
                            headers: {
                                "Content-Type": "application/json",
                            }
                        }
                    ).then((response) => {
                            setResultMessage("HumanBeing updated")
                        }
                    ).catch(error => {
                        if (error.response) {
                            if (error.response.status === 400) {
                                setResultMessage(error.response.data.message)
                            }

                            if (error.response.status === 401) {
                                setUser(null);
                                navigate("/");
                            }

                            if (error.response.status === 404) {
                                setResultMessage(error.response.data.message)
                            }
                        }
                    })
                    break;
                case ObjectTypes.CAR:
                    axios.patch(
                        `http://localhost:8080/car/${selectedId}`,
                        updateCar,
                        {
                            headers: {
                                "Content-Type": "application/json",
                            }
                        }
                    ).then((response) => {
                            setResultMessage("Car updated")
                        }
                    ).catch(error => {
                        if (error.response) {
                            if (error.response.status === 400) {
                                setResultMessage(error.response.data.message)
                            }

                            if (error.response.status === 401) {
                                setUser(null);
                                navigate("/");
                            }
                        }
                    })
                    break;
                case ObjectTypes.COORDINATES:
                    axios.patch(
                        `http://localhost:8080/coordinates/${selectedId}`,
                        updateCoordinates,
                        {
                            headers: {
                                "Content-Type": "application/json",
                            }
                        }
                    ).then((response) => {
                            setResultMessage("Coordinates updated")
                        }
                    ).catch(error => {
                        if (error.response) {
                            if (error.response.status === 400) {
                                setResultMessage(error.response.data.message)
                            }

                            if (error.response.status === 401) {
                                setUser(null);
                                navigate("/");
                            }
                        }
                    })
                    break;
                default:
            }
        }
    }

    return (
        <div>
            <button className="toggle-button" onClick={() => {
                setOpen(open === "update" ? "" : "update")
            }}>Update
            </button>
            {open === "update" && (
                <div className="modal-content">
                    <div className="object-type-selection">
                        <label>Choose object type:</label>
                        <select value={objectType} onChange={(e) => setObjectType(e.target.value)}>
                            <option value={ObjectTypes.HUMAN_BEING}>{ObjectTypes.HUMAN_BEING}</option>
                            <option value={ObjectTypes.CAR}>{ObjectTypes.CAR}</option>
                            <option value={ObjectTypes.COORDINATES}>{ObjectTypes.COORDINATES}</option>
                        </select>
                    </div>

                    <div className="search-container">
                        <div className="form-group">
                            <label>Enter ID:</label>
                            <input className="" type="text" value={selectedId}
                                   onChange={(e) => setSelectedId(e.target.value)}/>
                            <button className="submit-button" onClick={searchForObject}>Search</button>
                        </div>
                    </div>
                    {searchMessage && <p className="search-message">{searchMessage}</p>}

                    {objectFound && <div className="form-container">{renderForm()}</div>}

                    {objectFound && <button className="submit-button" onClick={sendRequest}>Submit</button>}
                    {objectFound && resultMessage && <p className="result-message">{resultMessage}</p>}
                </div>
            )}
        </div>
    )
}

export default UpdateModal;