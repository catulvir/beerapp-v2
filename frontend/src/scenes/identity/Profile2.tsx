import User from "../../types/identity/User";

interface UserProps {
    user?: User;
}

export const Profile2 = ({user}: UserProps) => {
    // const [user, setUser] = useState<User | null>();
    console.log(user);
    console.log(localStorage);

    /*useEffect(() => {
        const currentUser = AuthService.getCurrentUser();
        setUser(currentUser);

    }, []);*/

    return (
        <div className="container">
            {user &&
                <div>
                    <header className="jumbotron">
                        <h3>
                            <strong>{user.username}</strong> Profile
                        </h3>
                    </header>
                    <p>
                        <strong>Token:</strong>{" "}
                        {user.token.substring(0, 20)} ...{" "}
                        {user.token.substr(user.token.length - 20)}
                    </p>
                    <p>
                        <strong>Email:</strong>{" "}
                        {user.email}
                    </p>
                </div>
            }
        </div>
    );
}
