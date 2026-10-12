import {useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import {assets} from "../assets/assets.js";
import Input from "../components/Input.jsx";
import {validateEmail} from "../util/validation.js";
import axiosConfig from "../util/axiosConfig.jsx";
import {API_ENDPOINTS} from "../util/apiEndpoints.js";
import {LoaderCircle, MailCheck} from "lucide-react";
import Header from "../components/Header.jsx";

const Signup = () => {
    const [fullName, setFullName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [activationEmail, setActivationEmail] = useState("");

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsLoading(true);
        const normalizedEmail = email.trim().toLowerCase();

        //basic validation
        if (!fullName.trim()) {
            setError("Please enter your fullname");
            setIsLoading(false);
            return;
        }

        if (!validateEmail(normalizedEmail)) {
            setError("Please enter valid email address");
            setIsLoading(false);
            return;
        }

        if (!password.trim()) {
            setError("Please enter your password");
            setIsLoading(false);
            return;
        }

        setError("");

        //signup api call
        try {
            const response = await axiosConfig.post(API_ENDPOINTS.REGISTER, {
                fullName,
                email: normalizedEmail,
                password
            })
            if (response.status === 201) {
                setActivationEmail(normalizedEmail);
            }
        } catch(err) {
            setError(err.response?.data?.message || "Account Already Exits. Please Sign in.");
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <div className="h-screen w-full flex flex-col">
            <Header />
            <div className="flex-grow w-full relative flex items-center justify-center overflow-hidden">
                {/* Background image with blur*/}
                <img src={assets.login_bg} alt="Background" className="absolute inset-0 w-full h-full object-cover filter blur-sm" />

                <div className="relative z-10 w-full max-w-lg px-6">

                    <div className="bg-white bg-opacity-95 backdrop-blur-sm rounded-lg shadow-2xl p-8 max-h-[90vh] overflow-y-auto">
                        <h3 className="text-2xl font-semibold text-black text-center mb-2">
                            Create An Account
                        </h3>
                        <p className="text-sm text-slate-700 text-center mb-8">
                            Start tracking your spendings by joining with us.
                        </p>

                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div className="grid grid-cols-2 md:grid-cols-2 gap-4">
                                <Input
                                    value={fullName}
                                    onChange={(e) => setFullName(e.target.value)}
                                    label="Full Name"
                                    placeholder="Jhon Doe"
                                    type="text"
                                />

                                <Input
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    label="Email Address"
                                    placeholder="name@example.com"
                                    type="email"
                                />

                                <div className="col-span-2">
                                    <Input
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        label="Password"
                                        placeholder="*********"
                                        type="password"
                                    />
                                </div>

                            </div>
                            {error && (
                                <p className="text-red-800 text-sm text-center bg-red-50 p-2 rounded">
                                    {error}
                                </p>
                            )}

                            <button disabled={isLoading} className={`btn-primary w-full py-3 text-lg font-medium flex items-center justify-center gap-2 ${isLoading ? 'opacity-60 cursor-not-allowed': ''}`} type="submit">
                                {isLoading ? (
                                    <>
                                        <LoaderCircle className="animate-spin w-5 h-5" />
                                        Signing Up...
                                    </>
                                ): (
                                    "SIGN UP"
                                )}
                            </button>

                            <p className="text-sm text-slate-800 text-center mt-6">
                                Already have an account?
                                <Link to="/login" className="font-medium text-primary underline hover:text-primary-dark transition-colors">Login</Link>
                            </p>
                        </form>
                    </div>

                </div>


            </div>
            {activationEmail && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-gray-950/60 px-4 py-6">
                    <section
                        role="dialog"
                        aria-modal="true"
                        aria-labelledby="activation-success-title"
                        className="w-full max-w-md rounded-2xl bg-white p-6 text-center shadow-2xl sm:p-8"
                    >
                        <div className="mx-auto flex h-16 w-16 items-center justify-center rounded-full bg-purple-100 text-purple-700">
                            <MailCheck aria-hidden="true" className="h-8 w-8" />
                        </div>
                        <h2 id="activation-success-title" className="mt-5 text-2xl font-bold text-gray-900">
                            Account Created Successfully!
                        </h2>
                        <p className="mt-4 text-sm leading-6 text-gray-600 sm:text-base">
                            Your BudgetAi account has been created. We&apos;ve sent an activation link to{" "}
                            <a
                                href={`mailto:${activationEmail}`}
                                className="break-all font-semibold text-purple-700 underline"
                            >
                                {activationEmail}
                            </a>.
                        </p>
                        <p className="mt-3 text-sm leading-6 text-gray-600 sm:text-base">
                            Please check your inbox and click the activation link to activate your account before logging in. If you don&apos;t see the email, check your spam or junk folder.
                        </p>
                        <button
                            type="button"
                            onClick={() => navigate("/login")}
                            className="mt-6 w-full rounded-lg bg-purple-700 px-5 py-3 font-semibold text-white transition-colors hover:bg-purple-800 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 sm:w-auto"
                        >
                            Go to Login
                        </button>
                    </section>
                </div>
            )}
        </div>
    )
}

export default Signup;
