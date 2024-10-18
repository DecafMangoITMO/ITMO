import {useContext, useState} from "react";
import {ObjectTypes} from "./types";
import {useAuth} from "../../provider/AuthProvider";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import "../../create-modal.css"

const DeleteModal = ({open, setOpen}) => {
    const {setUser} = useAuth();
    const navigate = useNavigate();

    const [objectType, setObjectType] = useState(ObjectTypes.HUMAN_BEING);
    const [selectedId, setSelectedId] = useState('');
    const [resultMessage, setResultMessage] = useState('');

    const renderForm = () => {
        switch (objectType) {
            case ObjectTypes.HUMAN_BEING:
                return (
                    <h2>HumanBeing delete:</h2>
                )
            case ObjectTypes.CAR:
                return (
                    <h2>Car delete:</h2>
                )
            case ObjectTypes.COORDINATES:
                return (
                    <h2>Coordinates delete:</h2>
                )
        }
    }

    const validate = () => {
        return selectedId.match(/^[1-9][0-9]*$/) !== null;
    }

    const sendRequest = () => {
        if (validate()) {
            switch (objectType) {
                case ObjectTypes.HUMAN_BEING:
                    axios.delete(
                        `/human-being/${selectedId}`,
                        {}
                    ).then(response => {
                            setResultMessage("HumanBeing deleted")
                        }
                    ).catch(error => {
                        if (error.response) {
                            if (error.response.status === 401) {
                                setUser(null);
                                navigate("/");
                            }
                            if (error.response.status === 403) {
                                setResultMessage(error.response.data.message)
                            }
                            if (error.response.status === 404) {
                                setResultMessage(error.response.data.message)
                            }
                        }
                    })
                    break;
                case ObjectTypes.CAR:
                    axios.delete(
                        `/car/${selectedId}`,
                        {}
                    ).then(response => {
                            setResultMessage("Car deleted")
                        }
                    ).catch(error => {
                        if (error.response) {
                            if (error.response.status === 401) {
                                setUser(null);
                                navigate("/");
                            }
                            if (error.response.status === 403) {
                                setResultMessage(error.response.data.message)
                            }
                            if (error.response.status === 404) {
                                setResultMessage(error.response.data.message)
                            }
                        }
                    })
                    break;
                case ObjectTypes.COORDINATES:
                    axios.delete(
                        `/coordinates/${selectedId}`,
                        {}
                    ).then(response => {
                            setResultMessage("Coordinates deleted")
                        }
                    ).catch(error => {
                        if (error.response) {
                            if (error.response.status === 401) {
                                setUser(null);
                                navigate("/");
                            }
                            if (error.response.status === 403) {
                                setResultMessage(error.response.data.message)
                            }
                            if (error.response.status === 404) {
                                setResultMessage(error.response.data.message)
                            }
                        }
                    })
                    break;
                default:
            }
        } else {
            setResultMessage("Invalid id")
        }
    }

    return (
        <div>
            <button className="toggle-button" onClick={() => {
                setOpen(open === "delete" ? "" : "delete")
            }}>Delete
            </button>
            {open === "delete" && (
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

                    <div className="form-group">
                        <input type='text' value={selectedId} onChange={(event) => setSelectedId(event.target.value)}/>
                        <button className="submit-button" onClick={sendRequest}>Submit</button>
                        <p className="result-message">{resultMessage}</p>
                    </div>
                </div>)
            }
        </div>
    )
}

export default DeleteModal;