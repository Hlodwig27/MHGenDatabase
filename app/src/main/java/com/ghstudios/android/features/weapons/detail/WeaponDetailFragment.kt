package com.ghstudios.android.features.weapons.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ghstudios.android.ClickListeners.ItemClickListener
import com.ghstudios.android.components.ItemRecipeCell
import com.ghstudios.android.data.classes.Component
import com.ghstudios.android.data.classes.Weapon
import com.ghstudios.android.mhgendatabase.R
import com.ghstudios.android.util.applyArguments
import kotlinx.android.synthetic.main.fragment_weapon_detail.*


/**
 * Fragment that displays data information for a weapon.
 * Defers the internal data inflation and population to a subclass of WeaponDetailViewHolder.
 */
class WeaponDetailFragment : Fragment() {
    companion object {
        private const val ARG_WEAPON_ID = "WEAPON_ID"

        @JvmStatic fun newInstance(weaponId: Long): WeaponDetailFragment {
            return WeaponDetailFragment().applyArguments {
                putLong(ARG_WEAPON_ID, weaponId)
            }
        }
    }

    /**
     * Returns the viewmodel owned by the activity, which has already loaded weapon data
     */
    private val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(WeaponDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weapon_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.weaponData.observe(this, Observer(::populateWeapon))
        viewModel.createComponentData.observe(this, Observer(::populateCreateComponents))
        viewModel.improveComponentData.observe(this, Observer(::populateUpgradeComponents))
    }

    /**
     * Internal helper to populate the view with weapon data, and inflate the appropriate sub-view
     * based on the weapon type.
     */
    private fun populateWeapon(weapon: Weapon?) {
        if (weapon == null) return

        with(this.titlebar) {
            setIcon(weapon)
            setTitleText(weapon.name)
            setAltTitleText(getString(R.string.value_rare, weapon.rarityString))
        }
        this.weapon_description.text = weapon.description
        this.weapon_cost_create.text = "" + weapon.creationCost + "z"
        this.weapon_cost_upgrade.text = "" + weapon.upgradeCost + "z"

        // inflate the subview, depending on weapon type
        val weaponDataContainer = view!!.findViewById<ViewGroup>(R.id.weapon_detail_view)
        weaponDataContainer.removeAllViews()
        val weaponDataView = when (weapon.wtype) {
            Weapon.BOW -> WeaponBowDetailViewHolder(weaponDataContainer)
            Weapon.LIGHT_BOWGUN, Weapon.HEAVY_BOWGUN -> WeaponBowgunDetailViewHolder(weaponDataContainer)
            else -> WeaponBladeDetailViewHolder(weaponDataContainer)
        }
        weaponDataView.bindWeapon(weapon)
    }

    /**
     * Internal helper to populate the craft section with the correct components.
     * Use as a callback.
     */
    private fun populateCreateComponents(components: List<Component>?) {
        val view = this.view ?: return
        val section = view.findViewById<View>(R.id.create_section)
        val recipeView = view.findViewById<ItemRecipeCell>(R.id.create_recipe)
        populateRecipe(section, recipeView, components)
    }

    /**
     * Internal helper to populate the upgrade section with the correct components.
     * Use as a callback.
     */
    private fun populateUpgradeComponents(components: List<Component>?) {
        val view = this.view ?: return
        val section = view.findViewById<View>(R.id.upgrade_section)
        val recipeView = view.findViewById<ItemRecipeCell>(R.id.upgrade_recipe)
        populateRecipe(section, recipeView, components)
    }

    /**
     * Internal function to populate an arbitrary recipe
     * @param section Section to show or hide based on whether components is empty or not
     * @param recipeView The object to populate with recipe components
     * @param components The actual recipe items
     */
    private fun populateRecipe(section: View, recipeView: ItemRecipeCell, components: List<Component>?) {
        if (components == null || components.isEmpty()) {
            section.visibility = View.GONE
            return
        }

        section.visibility = View.VISIBLE
        for (component in components) {
            val item = component.component
            val itemCell = recipeView.addItem(item, item?.name, component.quantity, false)
            itemCell.setOnClickListener(ItemClickListener(context!!, item!!))
        }
    }
}
