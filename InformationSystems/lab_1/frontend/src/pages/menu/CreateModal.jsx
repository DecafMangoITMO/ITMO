import {useState} from "react";
import {MoodTypes, ObjectTypes, WeaponTypes} from "./types";
import axios from "axios";
import {useAuth} from "../../provider/AuthProvider";
import {useNavigate} from "react-router-dom";
import "../../create-modal.css"

const CreateModal = ({open, setOpen}) => {
    const {setUser} = useAuth();
    const navigate = useNavigate();

    const [objectType, setObjectType] = useState(ObjectTypes.HUMAN_BEING);
    const [createHumanBeing, setCreateHumanBeing] = useState({
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
    const [createHumanBeingErrors, setCreateHumanBeingErrors] = useState({
        name: "",
        coordinatesId: "",
        carId: "",
        impactSpeed: "",
        minutesOfWaiting: "",
    })

    const [createCar, setCreateCar] = useState({
        name: "",
    })
    const [createCarErrors, setCreateCarErrors] = useState({
        name: "",
    })

    const [createCoordinates, setCreateCoordinates] = useState({
        x: "",
        y: "",
    })
    const [createCoordinatesErrors, setCreateCoordinatesErrors] = useState({
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
                            <input type='text' value={createHumanBeing.name} onChange={(event) => {
                                setCreateHumanBeing({
                                    ...createHumanBeing,
                                    name: event.target.value,
                                })
                            }}/>
                            {createHumanBeingErrors.name && <span>{createHumanBeingErrors.name}</span>}
                        </div>
                        <div className="form-group">
                            <label>Coordinates ID:</label>
                            <input type='text' value={createHumanBeing.coordinatesId}
                                   onChange={(event) => {
                                       setCreateHumanBeing({
                                           ...createHumanBeing,
                                           coordinatesId: event.target.value,
                                       })
                                   }}/>
                            {createHumanBeingErrors.coordinatesId &&
                                <span>{createHumanBeingErrors.coordinatesId}</span>}
                        </div>
                        <div className="form-group">
                            <label>
                                <input type='checkbox' checked={createHumanBeing.realHero}
                                       onChange={(event) => {
                                           setCreateHumanBeing({
                                               ...createHumanBeing,
                                               realHero: event.target.checked,
                                           })
                                       }}/>
                                Real Hero
                            </label>
                        </div>
                        <div className="form-group">
                            <label>
                                <input type='checkbox' checked={createHumanBeing.hasToothpick}
                                       onChange={(event) => {
                                           setCreateHumanBeing({
                                               ...createHumanBeing,
                                               hasToothpick: event.target.checked,
                                           })
                                       }}/>
                                Has Toothpick
                            </label>
                        </div>
                        <div className="form-group">
                            <label>Car ID:</label>
                            <input type='text' value={createHumanBeing.carId} onChange={(event) => {
                                setCreateHumanBeing({
                                    ...createHumanBeing,
                                    carId: event.target.value,
                                })
                            }}/>
                            {createHumanBeingErrors.carId && <span>{createHumanBeingErrors.carId}</span>}
                        </div>
                        <div className="form-group">
                            <label>Mood:</label>
                            <select value={createHumanBeing.mood} onChange={(event) => {
                                setCreateHumanBeing({
                                    ...createHumanBeing,
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
                            <input type='text' value={createHumanBeing.impactSpeed}
                                   onChange={(event) => {
                                       setCreateHumanBeing({
                                           ...createHumanBeing,
                                           impactSpeed: event.target.value,
                                       })
                                   }}/>
                            {createHumanBeingErrors.impactSpeed && <span>{createHumanBeingErrors.impactSpeed}</span>}
                        </div>
                        <div className="form-group">
                            <label>Minutes of Waiting:</label>
                            <input type='text' value={createHumanBeing.minutesOfWaiting}
                                   onChange={(event) => {
                                       setCreateHumanBeing({
                                           ...createHumanBeing,
                                           minutesOfWaiting: event.target.value,
                                       })
                                   }}/>
                            {createHumanBeingErrors.minutesOfWaiting &&
                                <span>{createHumanBeingErrors.minutesOfWaiting}</span>}
                        </div>
                        <div className="form-group">
                            <label>Weapon Type:</label>
                            <select value={createHumanBeing.weaponType} onChange={(event) => {
                                setCreateHumanBeing({
                                    ...createHumanBeing,
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
                            <input type='text' value={createCar.name} onChange={(event) => {
                                setCreateCar({
                                    ...createCar,
                                    name: event.target.value,
                                })
                            }}/>
                            {createCarErrors.name && <span>{createCarErrors.name}</span>}
                        </div>
                    </form>
                )
            case ObjectTypes.COORDINATES:
                return (
                    <form className="modal-form">
                        <h2>Coordinates creation:</h2>
                        <div className="form-group">
                            <label>X:</label>
                            <input type='text' value={createCoordinates.x} onChange={(event) => {
                                setCreateCoordinates({
                                    ...createCoordinates,
                                    x: event.target.value,
                                })
                            }}/>
                            {createCoordinatesErrors.x && <span>{createCoordinatesErrors.x}</span>}
                        </div>
                        <div className="form-group">
                            <label>Y:</label>
                            <input type='text' value={createCoordinates.y} onChange={(event) => {
                                setCreateCoordinates({
                                    ...createCoordinates,
                                    y: event.target.value,
                                })
                            }}/>
                            {createCoordinatesErrors.y && <span>{createCoordinatesErrors.y}</span>}
                        </div>
                    </form>
                )
            default:
                return null
        }
    }

    const validateInput = () => {
        let valid = true;
        switch (objectType) {
            case ObjectTypes.HUMAN_BEING:
                let newCreateHumanBeingErrors = {};
                if (createHumanBeing.name.match(/^\s*$/) !== null) {
                    newCreateHumanBeingErrors.name = "Name must be between 6 and 10 characters";
                    valid = false
                }

                if (createHumanBeing.coordinatesId.match(/^[1-9][0-9]*$/) === null) {
                    newCreateHumanBeingErrors.coordinatesId = "CoordinatesId must be positive integer"
                    valid = false;
                }

                if (createHumanBeing.carId.match(/^[1-9][0-9]*$/) === null) {
                    newCreateHumanBeingErrors.carId = "CarId must be positive integer";
                    valid = false;
                }

                if (createHumanBeing.impactSpeed.match(/^-?\d+(\.\d+)?$/) === null) {
                    newCreateHumanBeingErrors.impactSpeed = "ImpactSpeed must be a number";
                    valid = false;
                }

                if (createHumanBeing.minutesOfWaiting.match(/^\s*$|^-?\d+(\.\d+)?$/) === null) {
                    newCreateHumanBeingErrors.minutesOfWaiting = "Minutes must be a number or empty";
                    valid = false;
                }

                setCreateHumanBeingErrors(newCreateHumanBeingErrors);
                return valid;
            case ObjectTypes.CAR:
                let newCreateCarErrors = {};
                if (createCar.name.match(/^\s+$/) !== null) {
                    newCreateCarErrors.name = "Name must not be blank";
                    valid = false;
                }
                setCreateCarErrors(newCreateCarErrors);
                return valid;
            case ObjectTypes.COORDINATES:
                let newCreateCoordinatesErrors = {};
                if (createCoordinates.x.match(/^-?\d+(\.\d+)?$/) === null || (createCoordinates.x.match(/^-?\d+(\.\d+)?$/) !== null && Number.parseFloat(createCoordinates.x) > 647)) {
                    newCreateCoordinatesErrors.x = "x must real number not more than 647";
                    valid = false;
                }

                if (createCoordinates.y.match(/^-?\d+$/) === null || (createCoordinates.y.match(/^-?\d+$/) !== null && Number.parseInt(createCoordinates.x) > 123)) {
                    newCreateCoordinatesErrors.y = "y must be integer not more than 123";
                    valid = false;
                }

                setCreateCoordinatesErrors(newCreateCoordinatesErrors);
                return valid;
        }
    }

    const sendRequest = () => {
        if (validateInput()) {
            switch (objectType) {
                case ObjectTypes.HUMAN_BEING:
                    axios.post(
                        "http://localhost:8080/human-being",
                        {
                            ...createHumanBeing,
                            realHero: !!createHumanBeing.realHero,
                            hasToothpick: !!createHumanBeing.hasToothpick,
                        },
                        {
                            headers: {
                                "Content-Type": "application/json",
                            }
                        }
                    ).then((response) => {
                            setResultMessage("New HumanBeing added")
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
                case ObjectTypes.CAR:
                    axios.post(
                        "http://localhost:8080/car",
                        createCar,
                        {
                            headers: {
                                "Content-Type": "application/json",
                            }
                        }
                    ).then((response) => {
                            setResultMessage("New Car added")
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
                    axios.post(
                        "http://localhost:8080/coordinates",
                        createCoordinates,
                        {
                            headers: {
                                "Content-Type": "application/json",
                            }
                        }
                    ).then((response) => {
                            setResultMessage("New Coordinates added")
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
                setOpen(open === "create" ? "" : "create")
            }}>Create
            </button>
            {open === "create" && (
                <div className="modal-content">
                    <div className="object-type-selection">
                        <label>Choose object type:</label>
                        <select value={objectType} onChange={(e) => setObjectType(e.target.value)}>
                            <option value={ObjectTypes.HUMAN_BEING}>{ObjectTypes.HUMAN_BEING}</option>
                            <option value={ObjectTypes.CAR}>{ObjectTypes.CAR}</option>
                            <option value={ObjectTypes.COORDINATES}>{ObjectTypes.COORDINATES}</option>
                        </select>
                    </div>

                    {renderForm()}

                    <button className="submit-button" onClick={sendRequest}>Submit</button>
                    <p className="result-message">{resultMessage}</p>
                </div>)
            }
        </div>
    )
}

export default CreateModal;