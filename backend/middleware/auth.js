const requiresAuth = (req, res, next) => {
    if (req.session.userId) {
        console.log(req.session.userId)
        next()
    } else {
        return res.status(400).json({ message: "Not logged in" });
    }
}

module.exports = { requiresAuth };
