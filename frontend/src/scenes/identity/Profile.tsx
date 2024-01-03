import { Component } from "react";
import { Navigate } from "react-router-dom";
import AuthService from "../../services/auth.service";
import User from "../../types/identity/User";

type Props = {};

type State = {
    redirect: string | null,
    userReady: boolean,
    currentUser: User | null
}

export default class Profile extends Component<Props, State> {
    constructor(props: Props) {
        super(props);

        this.state = {
            redirect: null,
            userReady: false,
            currentUser: null
        };
    }

    componentDidMount() {
        const currentUser = AuthService.getCurrentUser();

        if (!currentUser) this.setState({ redirect: "/login" });
        this.setState({ currentUser: currentUser!!, userReady: true })
    }

    render() {
        if (this.state.redirect) {
            return <Navigate to={this.state.redirect} />
        }

        const { currentUser } = this.state;

        return (
            <div className="container">
                {(this.state.userReady) ?
                    <div>
                        <header className="jumbotron">
                            <h3>
                                <strong>{currentUser!!.username} Profile</strong>
                            </h3>
                        </header>
                        <p>
                            <strong>Token:</strong>{" "}
                            {currentUser!!.token.substring(0, 20)} ...{" "}
                        </p>
                        <p>
                            <strong>Email:</strong>{" "}
                            {currentUser!!.email}
                        </p>
                    </div> : null}
            </div>
        );
    }
}
