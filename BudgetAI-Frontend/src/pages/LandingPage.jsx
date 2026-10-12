import Header from "../components/Header.jsx";
import HeroSection from "../components/HeroSection.jsx";
import ProductShowcase from "../components/ProductShowcase.jsx";
import {useCallback} from "react";
import {Link} from "react-router-dom";

const LandingPage = () => {
    const scrollToSection = useCallback((sectionId) => {
        document.getElementById(sectionId)?.scrollIntoView({behavior: "smooth"});
    }, []);

    return (
        <div className="bg-white font-sans text-gray-800">
            <Header onNavigate={scrollToSection} />
            <main>
                <HeroSection/>
                <ProductShowcase />
                <section id="about-us" className="scroll-mt-24 bg-purple-50 py-16 md:py-24">
                    <div className="container mx-auto max-w-5xl px-4">
                        <div className="mx-auto max-w-3xl text-center">
                            <p className="text-sm font-semibold uppercase tracking-wider text-purple-700">
                                About BudgetAi
                            </p>
                            <h2 className="mt-3 text-3xl font-bold tracking-tight text-gray-900 md:text-4xl">
                                Make every money decision with confidence
                            </h2>
                            <p className="mt-5 text-base leading-7 text-gray-600 md:text-lg">
                                BudgetAi brings your income and expenses together in one clear view.
                                Track transactions, understand your spending, and stay focused on the
                                financial goals that matter to you.
                            </p>
                        </div>
                        <div className="mt-10 grid gap-5 sm:grid-cols-3">
                            {[
                                ["See the full picture", "Keep income, expenses, and your balance easy to understand."],
                                ["Stay organized", "Group transactions into categories that make sense to you."],
                                ["Build better habits", "Use clear insights to make thoughtful day-to-day choices."]
                            ].map(([title, description]) => (
                                <article key={title} className="rounded-2xl border border-purple-100 bg-white p-6 shadow-sm">
                                    <h3 className="text-lg font-semibold text-gray-900">{title}</h3>
                                    <p className="mt-2 leading-6 text-gray-600">{description}</p>
                                </article>
                            ))}
                        </div>
                    </div>
                </section>
                <section id="contact-us" className="scroll-mt-24 py-16 md:py-24">
                    <div className="container mx-auto px-4">
                        <div className="mx-auto max-w-3xl rounded-3xl bg-gray-900 px-6 py-10 text-center text-white md:px-12 md:py-14">
                            <p className="text-sm font-semibold uppercase tracking-wider text-purple-300">
                                Contact Us
                            </p>
                            <h2 className="mt-3 text-3xl font-bold md:text-4xl">
                                We&apos;d love to hear from you
                            </h2>
                            <p className="mx-auto mt-4 max-w-xl leading-7 text-gray-300">
                                Have a question or an idea that could make managing money easier?
                                Get started with BudgetAi and explore the tools available in your account.
                            </p>
                            <Link
                                to="/signup"
                                className="mt-7 inline-flex rounded-lg bg-purple-600 px-6 py-3 font-semibold text-white transition-colors hover:bg-purple-500"
                            >
                                Get Started
                            </Link>
                        </div>
                    </div>
                </section>
            </main>
        </div>
    )
}

export default LandingPage;