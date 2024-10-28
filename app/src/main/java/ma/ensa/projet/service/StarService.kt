package ma.ensa.projet.service

import ma.ensa.projet.beans.Star
import ma.ensa.projet.dao.IDao

class StarService private constructor() : IDao<Star> {

    private val stars: MutableList<Star> = ArrayList()
    private object Holder { val INSTANCE = StarService() }

    companion object {
        @JvmStatic
        fun getInstance(): StarService = Holder.INSTANCE
    }

    override fun create(o: Star): Boolean {
        return stars.add(o)
    }

    override fun update(o: Star): Boolean {
        for (s in stars) {
            if (s.id == o.id) {
                s.img = o.img
                s.name = o.name
                s.star = o.star
                return true
            }
        }
        return false
    }

    override fun delete(o: Star): Boolean {
        return stars.remove(o)
    }

    override fun findById(id: Int): Star? {
        return stars.find { it.id == id }
    }

    override fun findAll(): List<Star> {
        return stars
    }
}
