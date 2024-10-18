import {useAuth} from "../provider/AuthProvider";
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import CreateModal from "./menu/CreateModal";
import DeleteModal from "./menu/DeleteModal";
import UpdateModal from "./menu/UpdateModal";
import AdvancedModal from "./menu/AdvancedModal";
import CanvasField from "./CanvasField";
import "../home.css";

const Home = () => {
    const {user, setUser} = useAuth();
    const navigate = useNavigate();

    const [open, setOpen] = useState('');
    const [submitAdminJoinRequestButtonIsActive, setSubmitAdminJoinRequestButtonIsActive] = useState(false);
    const [adminJoinRequestIsSent, setAdminJoinRequestIsSent] = useState(false);

    const [humanBeings, setHumanBeings] = useState([]);
    const [humanBeingsPage, setHumanBeingsPage] = useState(0);

    const [cars, setCars] = useState([]);
    const [carsPage, setCarsPage] = useState(0);

    const [coordinates, setCoordinates] = useState([]);
    const [coordinatesPage, setCoordinatesPage] = useState(0);

    const [adminJoinRequests, setAdminJoinRequests] = useState([]);
    const [adminJoinRequestsPage, setAdminJoinRequestsPage] = useState(0);

    const logout = () => {
        setUser(null);
    }

    const loadHumanBeings = (page) => {
        axios.get(
            `/human-being`,
            {
                params: {
                    from: page * 10,
                    size: 10
                }
            }
        ).then((response) => {
                setHumanBeings(response.data)
            }
        ).catch(error => {
            if (error.response) {
                if (error.response.status === 401) {
                    setUser(null);
                    navigate("/");
                }
            }
        })
    }

    const loadCars = (page) => {
        axios.get(
            `/car`,
            {
                params: {
                    from: page * 10,
                    size: 10
                }
            }
        ).then((response) => {
                setCars(response.data)
            }
        ).catch(error => {
            if (error.response) {
                if (error.response.status === 401) {
                    setUser(null);
                    navigate("/");
                }
            }
        })
    }

    const loadCoordinates = (page) => {
        axios.get(
            `/coordinates`,
            {
                params: {
                    from: page * 10,
                    size: 10
                }
            }
        ).then((response) => {
                setCoordinates(response.data)
            }
        ).catch(error => {
            if (error.response) {
                if (error.response.status === 401) {
                    setUser(null);
                    navigate("/");
                }
            }
        })
    }

    const loadAdminJoinRequests = (page) => {
        axios.get(
            `/admin`,
            {
                params: {
                    from: page * 10,
                    size: 10
                }
            }
        ).then((response) => {
                setAdminJoinRequests(response.data)
            }
        ).catch(error => {
            if (error.response) {
                if (error.response.status === 401) {
                    setUser(null);
                    navigate("/");
                }
            }
        })
    }

    const sendAdminJoinRequest = () => {
        axios.post(
            "/admin",
            {},
            {},
        ).then((response) => {
            }
        ).catch(error => {
            if (error.response) {
                if (error.response.status === 400) {
                    setAdminJoinRequestIsSent(true)
                }
            }
        })
    }

    const sendAdminJoinRequestApprove = (adminJoinRequestId) => {
        axios.put(
            `/admin/${adminJoinRequestId}`,
            {},
            {},
        ).then((response) => {
                const newAdminJoinRequests = adminJoinRequests.filter(request => request.id !== adminJoinRequestId);
                setAdminJoinRequests(newAdminJoinRequests);
            }
        ).catch(error => {
            if (error.response) {
                if (error.response.status === 401) {
                    setUser(null);
                    navigate("/");
                }
            }
        })
    }

    const loadUserRole = () => {
        axios.get(
            `/user/role/${user.username}`,
            {}
        ).then((response) => {
                user.role = response.data.role;
                setUser({
                    ...user,
                    role: response.data
                });
            }
        ).catch(error => {
            if (error.response) {
                if (error.response.status === 401) {
                    setUser(null);
                    navigate("/");
                }
            }
        })
    }

    useEffect(() => {
        const timer = setTimeout(() => {
            loadHumanBeings(0);
            loadCars(0);
            loadCoordinates(0);
            if (user.role === "ADMIN") {
                loadAdminJoinRequests(0);
            }
            const sock = new SockJS("/ws");
            const stompClient = Stomp.over(sock);

            stompClient.connect({
                headers: {
                    "Authorization": `Bearer ${user.token}`,
                }
            }, () => {
                stompClient.subscribe("/topic", (message) => {
                        loadHumanBeings(humanBeingsPage);
                        loadCars(carsPage);
                        loadCoordinates(coordinatesPage);
                        if (user.role === "ADMIN") {
                            loadAdminJoinRequests(adminJoinRequestsPage);
                        }
                        loadUserRole()
                        setAdminJoinRequestIsSent(false)
                    },
                    {
                        "Authorization": `Bearer ${user.token}`,
                    }
                )
            })

            setSubmitAdminJoinRequestButtonIsActive(true)
        }, 1000);
    }, []);

    return (
        <div className="home-container">
            <header className="header">
                <h1>Welcome, {user.username}!</h1>
                <p>Role: {user.role}</p>
                {user.role !== "ADMIN" && (
                    <button className="button" onClick={sendAdminJoinRequest}
                            disabled={!submitAdminJoinRequestButtonIsActive}>Send admin join request</button>)}
                <button className="button logout" onClick={logout}>Logout</button>
                {adminJoinRequestIsSent && (<p className='error-message'>Admin join request is already sent</p>)}
                <CreateModal open={open} setOpen={setOpen}/>
                <UpdateModal open={open} setOpen={setOpen} humanBeings={humanBeings} cars={cars}
                             coordinates={coordinates}/>
                <DeleteModal open={open} setOpen={setOpen}/>
                <AdvancedModal open={open} setOpen={setOpen}/>
                <CanvasField open={open} setOpen={setOpen} humanBeings={humanBeings}/>
            </header>
            <main>
                <h2>Data tables</h2>
                <div className="table-container">
                    <h3>Human Beings</h3>
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
                        {humanBeings.map(humanBeing => (
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
                    <div className="pagination">
                        <button className="button" onClick={() => {
                            if (humanBeingsPage > 0) {
                                loadHumanBeings(humanBeingsPage - 1);
                                setHumanBeingsPage(humanBeingsPage - 1);
                            }
                        }}>Prev
                        </button>
                        <button className="button" onClick={() => {
                            loadHumanBeings(humanBeingsPage + 1);
                            setHumanBeingsPage(humanBeingsPage + 1);
                        }}>Next
                        </button>
                    </div>
                </div>

                <div className="table-container">
                    <h3>Cars</h3>
                    <table className="styled-table">
                        <thead>
                        <tr>
                            <th>id</th>
                            <th>name</th>
                        </tr>
                        </thead>
                        <tbody>
                        {cars.map(car => (
                            <tr key={car.id}>
                                <td>{car.id}</td>
                                <td>{car.name}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                    <div className="pagination">
                        <button className="button" onClick={() => {
                            if (carsPage > 0) {
                                loadCars(carsPage - 1);
                                setCarsPage(carsPage - 1);
                            }
                        }}>Prev
                        </button>
                        <button className="button" onClick={() => {
                            loadCars(carsPage + 1);
                            setCarsPage(carsPage + 1);
                        }}>Next
                        </button>
                    </div>
                </div>

                <div className="table-container">
                    <h3>Coordinates</h3>
                    <table className="styled-table">
                        <thead>
                        <tr>
                            <th>id</th>
                            <th>x</th>
                            <th>y</th>
                        </tr>
                        </thead>
                        <tbody>
                        {coordinates.map(coordinates1 => (
                            <tr key={coordinates1.id}>
                                <td>{coordinates1.id}</td>
                                <td>{coordinates1.x}</td>
                                <td>{coordinates1.y}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                    <div className="pagination">
                        <button className="button" onClick={() => {
                            if (coordinatesPage > 0) {
                                loadCoordinates(coordinatesPage - 1);
                                setCoordinatesPage(coordinatesPage - 1);
                            }
                        }}>Prev
                        </button>
                        <button className="button" onClick={() => {
                            loadCoordinates(coordinatesPage + 1);
                            setCoordinatesPage(coordinatesPage + 1);
                        }}>Next
                        </button>
                    </div>
                </div>

                {user.role === "ADMIN" && (
                    <div className="table-container">
                        <h3>Admin Join Requests</h3>
                        <table className="styled-table">
                            <thead>
                            <tr>
                                <th>id</th>
                                <th>username</th>
                                <th>approve</th>
                            </tr>
                            </thead>
                            <tbody>
                            {adminJoinRequests.map(adminJoinRequest => (
                                <tr key={adminJoinRequest.id}>
                                    <td>{adminJoinRequest.id}</td>
                                    <td>{adminJoinRequest.username}</td>
                                    <td>
                                        <button className="button approve"
                                                onClick={() => sendAdminJoinRequestApprove(adminJoinRequest.id)}>Approve
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                        <div className="pagination">
                            <button className="button" onClick={() => {
                                if (adminJoinRequestsPage > 0) {
                                    loadAdminJoinRequests(adminJoinRequestsPage - 1);
                                    setAdminJoinRequestsPage(adminJoinRequestsPage - 1);
                                }
                            }}>Prev
                            </button>
                            <button className="button" onClick={() => {
                                loadAdminJoinRequests(adminJoinRequestsPage + 1);
                                setAdminJoinRequestsPage(adminJoinRequestsPage + 1);
                            }}>Next
                            </button>
                        </div>
                    </div>
                )}
            </main>
        </div>
    )
}

export default Home;