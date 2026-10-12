import {useState} from "react";
import {assets} from "../assets/assets.js";
import {Link} from "react-router-dom";
import {Menu, X} from "lucide-react";

const Header = ({onNavigate}) => {
    const [isMenuOpen, setIsMenuOpen] = useState(false);

    const navLinks = [
        { name: 'Home', id: 'home' },
        { name: 'About Us', id: 'about-us' },
        { name: 'Contact Us', id: 'contact-us' }
    ];

    const handleNavigation = (sectionId) => {
        onNavigate?.(sectionId);
        setIsMenuOpen(false);
    };

    const renderNavigationLink = (link) => (
        onNavigate ? (
            <button
                key={link.id}
                type="button"
                onClick={() => handleNavigation(link.id)}
                className="text-left text-gray-600 hover:text-purple-600 transition-colors"
            >
                {link.name}
            </button>
        ) : (
            <Link
                key={link.id}
                to={`/home#${link.id}`}
                className="text-gray-600 hover:text-purple-600 transition-colors"
            >
                {link.name}
            </Link>
        )
    );

    return (
        <header className="border-b border-gray-200">
            <div className="container mx-auto px-4">
                <div className="flex items-center justify-between h-20">
                    {/* Logo */}
                    <div className="flex items-center gap-2">
                        <img src={assets.logo} alt="logo" className="h-10 w-10" />
                        <span className="text-lg font-bold text-black truncate">BudgetAi</span>
                    </div>

                    {/* Desktop Navigation */}
                    <nav className="hidden lg:flex items-center space-x-8">
                        {navLinks.map(renderNavigationLink)}
                    </nav>

                    {/* Action Buttons & Hamburger Menu */}
                    <div className="flex items-center space-x-4">
                        <div className="hidden sm:flex items-center space-x-4">
                            <Link to="/login" className="text-gray-600 hover:text-purple-600 transition-colors">
                                Login
                            </Link>
                            <Link
                                to="/signup"
                                className="bg-purple-600 text-white px-4 py-2 rounded-lg font-semibold hover:bg-purple-700 transition-colors"
                            >
                                Get Started
                            </Link>
                        </div>
                        <button
                            onClick={() => setIsMenuOpen(!isMenuOpen)}
                            className="lg:hidden p-2 rounded-md text-gray-600 hover:bg-gray-100"
                            aria-label="Toggle menu"
                        >
                            {isMenuOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
                        </button>
                    </div>
                </div>
            </div>

            {/* Mobile Navigation */}
            {isMenuOpen && (
                <div className="lg:hidden bg-white border-t border-gray-200">
                    <div className="container mx-auto px-4 py-4">
                        <nav className="flex flex-col space-y-4">
                            {navLinks.map(renderNavigationLink)}
                            <div className="flex flex-col space-y-3 pt-4 border-t border-gray-100">
                                <Link to="/login" onClick={() => setIsMenuOpen(false)} className="text-gray-600 hover:text-purple-600 transition-colors w-full text-left">
                                    Login
                                </Link>
                                <Link
                                    to="/signup"
                                    onClick={() => setIsMenuOpen(false)}
                                    className="bg-purple-600 text-white px-4 py-2 rounded-lg font-semibold hover:bg-purple-700 transition-colors text-center"
                                >
                                    Get Started
                                </Link>
                            </div>
                        </nav>
                    </div>
                </div>
            )}
        </header>
    );
};

export default Header;